package kitchenpos.order;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.dto.OrderRequest;
import org.springframework.http.MediaType;

public class OrderAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 주문_생성_요청(OrderRequest params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> createResponse, OrderRequest request) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all().body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location + "/order-status")
                .then().log().all()
                .extract();
    }

    public void 주문_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public static void 주문_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }

    public void 주문_응답_실패(ExtractableResponse<Response> response) {
        HttpStatusAssertion.INTERNAL_SERVER_ERROR(response);
    }
}
