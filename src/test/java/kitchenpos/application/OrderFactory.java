package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Order;
import kitchenpos.dto.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderFactory {

    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/orders")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문_상태변경_요청(OrderRequest params, Long orderId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put("/api/orders/"+orderId+"/order-status")
                .then().log().all().
                extract();
    }

}
