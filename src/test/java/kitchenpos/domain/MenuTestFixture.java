package kitchenpos.domain;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuTestFixture {

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(null, name, price, menuGroupId, menuProducts);
    }

    public static void 주문_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderIds = response.jsonPath().getList(".", Order.class).stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderIds).containsAll(expectedOrderIds);
    }

    public static void 주문_상태_변경됨(ExtractableResponse<Response> response, String expectOrderStatus) {
        String actualOrderStatus = response.jsonPath().getString("orderStatus");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualOrderStatus).isEqualTo(expectOrderStatus)
        );
    }
}
