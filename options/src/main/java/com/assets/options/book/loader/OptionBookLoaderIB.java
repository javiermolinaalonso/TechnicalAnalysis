package com.assets.options.book.loader;

public class OptionBookLoaderIB {

    public static void main(String[] args) {
//        List<IBMarketDataResponse> ibMarketDataResponses
    }

//    private List<Option> map(List<IBOptionDataResponse> options, String ticker, double currentPrice) {
//        List<Long> conids = options.stream().map(IBOptionDataResponse::getConid).collect(Collectors.toList());
//        List<IBMarketDataResponse> ibMarketDataResponses = restTemplate.loadMarketData(conids, asList("7633"));
//
//        Map<Long, List<IBMarketDataResponse>> collect = ibMarketDataResponses.stream().collect(Collectors.groupingBy(IBMarketDataResponse::getConid));
//
//        return options.stream()
//                .map(
//                        o -> {
//                            OptionBuilder optionBuilder = OptionBuilder.create(ticker, currentPrice)
//                                    .withCurrentDate(LocalDate.now(clock))
//                                    .withStrikePrice(Double.parseDouble(o.getStrike()))
//                                    .withExpirationAt(LocalDate.parse(o.getMaturityDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
//                                    .withBidAsk(Double.parseDouble(collect.get(o.getConid()).get(0).getBidPrice()), Double.parseDouble(collect.get(o.getConid()).get(0).getAskPrice()))
//                                    .withIV(Double.parseDouble(collect.get(o.getConid()).get(0).getImpliedvolatility().replace("%", "")) / 100);
//                            if (o.getRight().equals("C")) {
//                                return optionBuilder.buildCall();
//                            } else {
//                                return optionBuilder.buildPut();
//                            }
//                        }
//                ).collect(Collectors.toList());
//    }

//    return OptionBook.Builder.create()
//            .withOptions(map(options, ibSearchResponse.getSymbol(), lastPrice.doubleValue()))
//            .withTicker(ibSearchResponse.getSymbol())
//            .withNow(LocalDate.now(clock))
//            .withCurrentPrice(lastPrice)
//                    .build();
}
