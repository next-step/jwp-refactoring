package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.fixture.acceptance.AcceptanceTestMenuFixture;
import kitchenpos.fixture.acceptance.AcceptanceTestOrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends BaseAcceptanceTest {
    private AcceptanceTestMenuFixture 메뉴;
    private AcceptanceTestOrderTableFixture 테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        메뉴 = new AcceptanceTestMenuFixture();
        테이블 = new AcceptanceTestOrderTableFixture();
    }

    @DisplayName("주문을 관리한다")
    @Test
    void manageOrder() {
        // given
        final Map<MenuResponse, Long> 메뉴_수량1 = new HashMap<>();
        메뉴_수량1.put(메뉴.돼지모듬, 1L);
        메뉴_수량1.put(메뉴.김치찌개정식, 1L);

        final Map<MenuResponse, Long> 메뉴_수량2 = new HashMap<>();
        메뉴_수량2.put(메뉴.김치찌개정식, 2L);

        // when
        ExtractableResponse<Response> created1 = 주문_생성_요청(테이블.테이블1.getId(), 메뉴_수량1);
        // then
        주문_생성됨(created1);

        // when
        ExtractableResponse<Response> created2 = 주문_생성_요청(테이블.테이블2.getId(), 메뉴_수량2);
        // then
        주문_생성됨(created2);

        // when
        ExtractableResponse<Response> list = 주문_목록_조회_요청();
        // then
        주문_목록_조회됨(list);

        // when
        ExtractableResponse<Response> statusUpdated = 주문_상태_변경_요청(created1.as(OrderResponse.class), OrderStatus.MEAL.name());
        // then
        주문_상태_변경됨(statusUpdated);
    }

    public static ExtractableResponse<Response> 주문_생성_요청(final Long orderTableId, final Map<MenuResponse, Long> menuQuantity) {
        final Map<String, Object> body = new HashMap<>();
        body.put("orderTableId", orderTableId);
        body.put("orderLineItems", convertToOrderLineItemsParam(menuQuantity));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private static List<Map<String, Object>> convertToOrderLineItemsParam(final Map<MenuResponse, Long> productQuantity) {
        return productQuantity.entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> menuProduct = new HashMap<>();
                    menuProduct.put("menuId", entry.getKey().getId());
                    menuProduct.put("quantity", entry.getValue());
                    return menuProduct;
                })
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(final OrderResponse order, final String orderStatus) {
        final Map<String, Object> body = new HashMap<>();
        body.put("id", order.getId());
        body.put("orderTableId", order.getOrderTableId());
        body.put("orderStatus", orderStatus);
        body.put("orderedTime", order.getOrderedTime());
        body.put("orderLineItems", order.getOrderLineItems());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().put("/api/orders/{orderId}/order-status", order.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_상태_변경됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
