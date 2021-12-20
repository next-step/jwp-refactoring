package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.order.Order;
import org.springframework.http.MediaType;

public class OrderAcceptanceStep {

    private static final String API_URL = "/api/orders";

    private OrderAcceptanceStep() {
    }

    public static ExtractableResponse<Response> 주문_등록_요청(Order order) {
        return RestAssured
            .given().log().all()
            .body(order)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(API_URL)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 주문_상태변경_요청(Long orderId, Order changeOrder) {
        return RestAssured
            .given().log().all()
            .body(changeOrder)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(API_URL + "/" + orderId + "/order-status")
            .then().log().all()
            .extract();
    }

    public static Order 주문_등록_검증(ExtractableResponse<Response> response, Order expected) {
        Order 등록된_주문 = response.as(Order.class);

        assertThat(등록된_주문.getId()).isNotNull();
        assertThat(등록된_주문.getOrderLineItems().size()).isEqualTo(
            expected.getOrderLineItems().size());

        return 등록된_주문;
    }

    public static List<Order> 주문_목록조회_검증(ExtractableResponse<Response> response, Order expected) {
        List<Order> 조회된_주문_목록 = response.as(new TypeRef<List<Order>>() {
        });
        assertThat(조회된_주문_목록).contains(expected);

        return 조회된_주문_목록;
    }


    public static void 주문_상태변경_검증(ExtractableResponse<Response> response, String expected) {
        Order 변경된_주문 = response.as(Order.class);
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(expected);
    }

}
