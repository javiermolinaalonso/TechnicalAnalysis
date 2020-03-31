package com.assets.options.book.loader;

import com.assets.options.book.OptionBook;
import com.assets.options.book.loader.ib.OptionBookLoaderIB;
import com.assets.options.entities.Option;
import org.hamcrest.number.BigDecimalCloseTo;
import org.hamcrest.number.IsCloseTo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.List;

import static com.assets.options.main.ObjectMapperProvider.restTemplate;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class OptionBookLoaderIBTestIT {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 5001);

    @Before
    public void setUp() throws Exception {
        String spy_search = Files.readString(Path.of(this.getClass().getResource("/ib/search_spy_response.json").toURI()));

        MockServerClient serverClient = new MockServerClient("localhost", mockServerRule.getPort());
        serverClient.when(
                HttpRequest.request("/v1/portal/iserver/secdef/search")
                        .withMethod("POST")
                        .withQueryStringParameter("symbol", "SPY"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(spy_search)
                );

        String spy_data = Files.readString(Path.of(this.getClass().getResource("/ib/spy_data_for_month_and_strike.json").toURI()));
        serverClient.when(
                HttpRequest.request("/v1/portal/iserver/secdef/info")
                        .withMethod("GET")
                        .withQueryStringParameters(
                                Parameter.param("conid", "756733"),
                                Parameter.param("sectype", "OPT"),
                                Parameter.param("month", "APR20"),
                                Parameter.param("strike", "220")
                        )
        )
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(spy_data)
                );

        String strikes = Files.readString(Path.of(this.getClass().getResource("/ib/strikes.json").toURI()));
        serverClient.when(
                HttpRequest.request("/v1/portal/iserver/secdef/strikes")
                        .withMethod("GET")
                        .withQueryStringParameters(
                                Parameter.param("conid", "756733"),
                                Parameter.param("secType", "OPT"),
                                Parameter.param("month", "APR20")
                        )
        )
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(strikes)
                );


        String optionData = Files.readString(Path.of(this.getClass().getResource("/ib/market_data_options.json").toURI()));
        serverClient.when(
                HttpRequest.request("/v1/portal/iserver/marketdata/snapshot")
                        .withMethod("GET")
                        .withQueryStringParameters(
                                Parameter.param("conids", "392376410,406628075"),
                                Parameter.param("fields", "7633")
                        )
        )
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(optionData)
                );

        String spyData = Files.readString(Path.of(this.getClass().getResource("/ib/market_data_spy.json").toURI()));
        serverClient.when(
                HttpRequest.request("/v1/portal/iserver/marketdata/snapshot")
                        .withMethod("GET")
                        .withQueryStringParameters(
                                Parameter.param("conids", "756733")
                        )
        )
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(spyData)
                );
    }

    @Test
    public void givenSymbolRequest_whenRequest_expectDataIsLoaded() {
        OptionBookLoaderIB victim = new OptionBookLoaderIB(restTemplate(), "http://localhost:5001", Clock.fixed(LocalDateTime.of(2020, 3, 28, 20, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
        OptionBook optionBook = victim.load("SPY");
        assertThat(optionBook.getCurrentPrice(), BigDecimalCloseTo.closeTo(BigDecimal.valueOf(262.64), BigDecimal.ONE));
        assertThat(optionBook.getNow(), equalTo(LocalDate.of(2020, 3, 28)));
        assertThat(optionBook.getAvailableDates(), containsInAnyOrder(LocalDate.of(2020, 4, 17), LocalDate.of(2020, 4, 1)));
        assertThat(optionBook.getTicker(), equalTo("SPY"));
        assertOptions(optionBook.getOptions());
    }

    private void assertOptions(List<Option> options) {
        Option option = options.get(1);
        assertThat(option.getBid(), BigDecimalCloseTo.closeTo(BigDecimal.valueOf(38.22), BigDecimal.valueOf(0.1)));
        assertThat(option.getAsk(), BigDecimalCloseTo.closeTo(BigDecimal.valueOf(38.91), BigDecimal.valueOf(0.1)));
        assertThat(option.getStrikePrice(), BigDecimalCloseTo.closeTo(BigDecimal.valueOf(220), BigDecimal.ONE));
        assertThat(option.getExpirationDate(), equalTo(LocalDate.of(2020,4,17)));
        assertThat(option.getImpliedVolatility(), IsCloseTo.closeTo(0.0015, 0.0001));
        assertThat(option.isCall(), is(true));
    }
}
