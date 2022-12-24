package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.domain.OrderTableEmpty;
import kitchenpos.tablegroup.domain.OrderTableGuests;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTableRestAssured {
    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_empty_수정_요청(Long orderTableId, boolean empty) {
        OrderTableEmpty request = new OrderTableEmpty(empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님수_수정_요청(Long orderTableId, int numberOfGuests) {
        OrderTableGuests request = new OrderTableGuests(numberOfGuests);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
