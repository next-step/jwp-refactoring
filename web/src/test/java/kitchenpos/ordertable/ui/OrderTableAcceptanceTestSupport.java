package kitchenpos.ordertable.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.http.MediaType;

public class OrderTableAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 주문_테이블_등록_되어있음(int numberOfGuests, boolean empty) {
        OrderTableRequest params = new OrderTableRequest(numberOfGuests, empty);
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(params);
        주문_테이블_생성_완료(response);
        return response;
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_주문_상태_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest request) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 주문_테이블_방문한_손님_수_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest params) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public static void 주문_테이블_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }
}
