package com.assets.options.book.loader.ib;

import com.assets.options.book.OptionBook;
import com.assets.options.entities.Option;
import com.assets.options.entities.OptionBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class OptionBookLoaderIB {

    private static final String SEARCH_SYMBOL = "%s/v1/portal/iserver/secdef/search?symbol=";
    private static final String GET_STRIKES = "%s/v1/portal/iserver/secdef/strikes?conid=%s&sectype=OPT&month=%s";
    private static final String GET_OPTION_DATA = "%s/v1/portal/iserver/secdef/info?conid=%s&sectype=OPT&month=%s&strike=%s";
    private static final String GET_MARKET_DATA = "%s/v1/portal/iserver/marketdata/snapshot";
    public static final String SEC_TYPE_OPTION = "OPT";

    private final RestTemplate restTemplate;
    private final String host;
    private final Clock clock;

    public OptionBookLoaderIB(RestTemplate restTemplate) {
        this(restTemplate, "https://localhost:5000", Clock.systemUTC());
    }

    public OptionBookLoaderIB(RestTemplate restTemplate, String host, Clock clock) {
        this.restTemplate = restTemplate;
        this.host = host;
        this.clock = clock;
    }

    public OptionBook load(String symbol) {
        String searchUri = String.format(SEARCH_SYMBOL, host) + symbol;
        IBSearchResponse ibSearchResponse = restTemplate.postForObject(searchUri, emptyMap(), IBSearchResponse[].class)[0];
        IBMarketDataResponse marketDataResponse = loadMarketData(Map.of("conids", ibSearchResponse.getConid()))[0];
        //GET data
        List<IBOptionDataResponse> options = getOptionsFor(ibSearchResponse);
        //Need to retrieve the market data
        BigDecimal lastPrice = new BigDecimal(marketDataResponse.getLastPrice());

        return OptionBook.Builder.create()
                .withOptions(map(options, ibSearchResponse.getSymbol(), lastPrice.doubleValue()))
                .withTicker(ibSearchResponse.getSymbol())
                .withNow(LocalDate.now(clock))
                .withCurrentPrice(lastPrice)
                .build();
    }

    private IBMarketDataResponse[] loadMarketData(Map<String, Object> variables) {
        String url = getMarketDataUrl(variables);
        IBMarketDataResponse[] foo = restTemplate.getForObject(url, IBMarketDataResponse[].class);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return restTemplate.getForObject(url, IBMarketDataResponse[].class);
    }

    private String getMarketDataUrl(Map<String, Object> variables) {
        String searchUri = String.format(GET_MARKET_DATA, host);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(searchUri);
        variables.forEach((k, v) -> uriComponentsBuilder.queryParam(k, v));
        return uriComponentsBuilder.build().toUriString();
    }
    private List<Option> map(List<IBOptionDataResponse> options, String ticker, double currentPrice) {
        String conids = options.stream().map(o -> String.valueOf(o.getConid())).collect(Collectors.joining(","));
        IBMarketDataResponse[] ibMarketDataResponses = loadMarketData(Map.of("conids", conids, "fields", "7633"));

        Map<Long, List<IBMarketDataResponse>> collect = Arrays.stream(ibMarketDataResponses).collect(Collectors.groupingBy(IBMarketDataResponse::getConid));

        return options.stream()
                .map(
                        o -> {
                            OptionBuilder optionBuilder = OptionBuilder.create(ticker, currentPrice)
                                    .withCurrentDate(LocalDate.now(clock))
                                    .withStrikePrice(Double.parseDouble(o.getStrike()))
                                    .withExpirationAt(LocalDate.parse(o.getMaturityDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                    .withBidAsk(Double.parseDouble(collect.get(o.getConid()).get(0).getBidPrice()), Double.parseDouble(collect.get(o.getConid()).get(0).getAskPrice()))
                                    .withIV(Double.parseDouble(collect.get(o.getConid()).get(0).getImpliedvolatility().replace("%", "")) / 100);
                            if (o.getRight().equals("C")) {
                                return optionBuilder.buildCall();
                            } else {
                                return optionBuilder.buildPut();
                            }
                        }
                ).collect(Collectors.toList());
    }

    private List<IBOptionDataResponse> getOptionsFor(IBSearchResponse ibSearchResponse) {
        List<String> months = ibSearchResponse.getSections().stream()
                .filter(t -> t.getSecType().equals(SEC_TYPE_OPTION))
                .findFirst()
                .map(section -> section.getMonths())
                .map(month -> Stream.of(month.split(";")).collect(Collectors.toList()))
                .orElse(emptyList());

        List<IBOptionDataResponse> response = new ArrayList<>();
        for (String month : months) {
            if (StringUtils.isNotBlank(month)) {
                String strikesUri = String.format(GET_STRIKES, host, ibSearchResponse.getConid(), month);
                IBStrikesResponse ibStrikesResponse = restTemplate.getForObject(strikesUri, IBStrikesResponse.class);

                response.addAll(getOptionsFor(ibSearchResponse.getConid(), month, ibStrikesResponse));
            }
        }
        return response;
    }

    private Set<IBOptionDataResponse> getOptionsFor(String conid, String month, IBStrikesResponse ibStrikesResponse) {
        Set<IBOptionDataResponse> response = new HashSet<>();
        for (String strike : ibStrikesResponse.getCalls()) {
            String uri = String.format(GET_OPTION_DATA, host, conid, month, strike);
            IBOptionDataResponse[] forObject = restTemplate.getForObject(uri, IBOptionDataResponse[].class);
            for (IBOptionDataResponse ibOptionDataResponse : forObject) {
                response.add(ibOptionDataResponse);
            }
        }

        for (String strike : ibStrikesResponse.getPuts()) {
            String uri = String.format(GET_OPTION_DATA, host, conid, month, strike);
            IBOptionDataResponse[] forObject = restTemplate.getForObject(uri, IBOptionDataResponse[].class);
            for (IBOptionDataResponse ibOptionDataResponse : forObject) {
                response.add(ibOptionDataResponse);
            }
        }
        return response;
    }

}
