package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderAcceptanceTest extends AcceptanceTest {
    private OrderRequest orderRequest;
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderLineItems.add(new OrderLineItem(new Menu(2L), 2));
        orderLineItems.add(new OrderLineItem(new Menu(1L), 1));

        orderRequest = new OrderRequest(2L, orderLineItems);
    }

    @DisplayName("주문을 관리한다")
    @Test
    void manageOrder() {
        //주문 등록
        ExtractableResponse<Response> createResponse = 주문_등록_요청(orderRequest);
        주문_등록됨(createResponse);
        //주문 내역 조회
        ExtractableResponse<Response> findResponse = 주문내역_조회_요청();
        주문내역_조회됨(findResponse);
        //주문 상태 변경
        ExtractableResponse<Response> changeResponse = 주문상태_변경_요청(createResponse, OrderStatus.MEAL.name());
        주문상태_변경됨(changeResponse);
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(ExtractableResponse<Response> response, String orderStatus) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                body(orderStatus).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().put(uri + "/order-status").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문_등록_요청(OrderRequest orderRequest) {
        return RestAssured.given().log().all().
                body(orderRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/orders").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 주문내역_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/orders").
                then().log().all().
                extract();
    }

    private void 주문_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(OrderResponse.class).getOrderTableResponse().getId()).isEqualTo(2L);
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
    }

    private void 주문내역_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderResponse> orders = response.jsonPath().getList(".", OrderResponse.class);
        List<Long> orderTableIds = orders.stream()
                .map(order -> order.getOrderTableResponse().getId())
                .collect(Collectors.toList());

        assertThat(orderTableIds).contains(2L);
    }

    private void 주문상태_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(OrderStatus.MEAL.toString());
    }
}
