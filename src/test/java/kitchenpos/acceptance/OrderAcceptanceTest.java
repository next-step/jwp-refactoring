package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.응답코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends BaseAcceptanceTest{

    @DisplayName("주문을 관리한다")
    @Test
    public void manageOrder() {
        //메뉴 생성
        //given
        Order order = new Order(1l);
        order.addOrderItems(new OrderLineItem(1l, 1));
        //when
        ExtractableResponse<Response> 주문_생성_요청 = 주문_생성_요청(order);
        //then
        응답코드_확인(주문_생성_요청, HttpStatus.CREATED);

        //메뉴 그룹 조회
        //when
        ExtractableResponse<Response> 주문_목록_조회_요청 = 주문_목록_조회_요청();
        //then
        응답코드_확인(주문_목록_조회_요청, HttpStatus.OK);
        주문_조회됨(주문_목록_조회_요청, 주문_생성_요청.as(Order.class).getId());

        //메뉴 상태 변경
        //given
        Order 변경주문 = new Order(1l);
        변경주문.changeOrderStatus(OrderStatus.MEAL);
        //when
        ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(주문_생성_요청.as(Order.class).getId(), 변경주문);
        //then
        응답코드_확인(주문_상태_변경_요청, HttpStatus.OK);
        상태_변경됨(주문_상태_변경_요청, OrderStatus.MEAL);

    }

    public static ExtractableResponse<Response>주문_생성_요청(Order order) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().post("/api/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/orders")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, Order order) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(order)
            .when().put("/api/orders/{orderId}/order-status", orderId)
            .then().log().all()
            .extract();
    }

    public static void 주문_조회됨(final ExtractableResponse<Response> response, Long id) {
        assertThat(response.jsonPath().getList(".", Order.class).stream().anyMatch(order -> order.getId().equals(id))).isTrue();
    }

    public static void 상태_변경됨(final ExtractableResponse<Response> response, OrderStatus orderStats) {
        assertThat(response.as(Order.class).getOrderStatus()).isEqualTo(orderStats);
    }

    private List<OrderLineItem> createOrderLineItem() {
        return new ArrayList<>(Arrays.asList(new OrderLineItem( 1l, 1)));
    }
}
