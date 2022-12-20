package kitchenpos.ordertable.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderEmpty;
import kitchenpos.domain.OrderGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        OrderEmpty request = new OrderEmpty(empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님수_수정_요청(Long orderTableId, int numberOfGuests) {
        OrderGuests request = new OrderGuests(numberOfGuests);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
