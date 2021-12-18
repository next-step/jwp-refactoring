package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Order;
import org.springframework.http.MediaType;

public class OrderAcceptanceStep {

    private static final String ORDER_API_URL = "/api/orders";

    private OrderAcceptanceStep() {
    }

    public static ExtractableResponse<Response> 주문_등록_요청(Order 등록요청_주문) {
        return RestAssured
            .given().log().all()
            .body(등록요청_주문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(ORDER_API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(ORDER_API_URL)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 주문_상태변경_요청(Long 주문번호, Order 상태변경주문) {
        return RestAssured
            .given().log().all()
            .body(상태변경주문)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(ORDER_API_URL + "/" + 주문번호 + "/order-status")
            .then().log().all()
            .extract();
    }

    public static Order 주문_등록_검증(ExtractableResponse<Response> 주문_등록_결과, Order 예상_주문) {
        Order 등록된_주문 = 주문_등록_결과.as(Order.class);

        assertThat(등록된_주문.getId()).isNotNull();
        assertThat(등록된_주문.getOrderLineItems().size()).isEqualTo(예상_주문.getOrderLineItems().size());

        return 등록된_주문;
    }

    public static List<Order> 주문_목록조회_검증(ExtractableResponse<Response> 주문_목록조회_결과, Order 등록된_주문) {
        List<Order> 조회된_주문_목록 = 주문_목록조회_결과.as(new TypeRef<List<Order>>() {
        });
        assertThat(조회된_주문_목록).contains(등록된_주문);

        return 조회된_주문_목록;
    }


    public static void 주문_상태변경_검증(ExtractableResponse<Response> 주문_상태변경_결과, String 예상_주문상태) {
        Order 변경된_주문 = 주문_상태변경_결과.as(Order.class);
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(예상_주문상태);
    }

}
