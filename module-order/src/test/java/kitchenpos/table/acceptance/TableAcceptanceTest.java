package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableAcceptanceTest extends AcceptanceTest {
    private static final String API_URL = "/api/tables";

    private static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTableRequest = OrderTableRequest.of(numberOfGuests, empty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    public static OrderTableResponse 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        ExtractableResponse<Response> response = 테이블_생성_요청(numberOfGuests, empty);

        return response.as(OrderTableResponse.class);
    }

    private void 테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Long 테이블_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 테이블_상태_수정(Long orderTableId, boolean empty) {
        ChangeEmptyRequest changeEmptyRequest = ChangeEmptyRequest.of(empty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeEmptyRequest)
                .when().put(String.format("%s/{orderTableId}/empty", API_URL), orderTableId)
                .then().log().all()
                .extract();
    }

    private void 테이블_상태_수정됨(ExtractableResponse<Response> response, boolean expect) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getBoolean("empty")).isEqualTo(expect)
        );
    }

    private ExtractableResponse<Response> 테이블_인원_수정(Long orderTableId, int numberOfGuests) {
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = ChangeNumberOfGuestsRequest.of(numberOfGuests);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeNumberOfGuestsRequest)
                .when().put(String.format("%s/{orderTableId}/number-of-guests", API_URL), orderTableId)
                .then().log().all()
                .extract();
    }

    private void 테이블_인원_수정됨(ExtractableResponse<Response> response, int expect) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(expect)
        );
    }

    @Test
    @DisplayName("테이블 관리 기능 (테이블 생성, 조회, 상태 수정)")
    void table() {
        ExtractableResponse<Response> 테이블_생성_요청_결과 = 테이블_생성_요청(4, true);

        테이블_생성됨(테이블_생성_요청_결과);

        ExtractableResponse<Response> 테이블_목록_조회_요청_결과 = 테이블_목록_조회_요청();

        테이블_목록_조회됨(테이블_목록_조회_요청_결과);

        Long 테이블_ID = 테이블_ID_조회(테이블_생성_요청_결과);

        boolean 테이블_사용중 = false;
        ExtractableResponse<Response> 테이블_상태_수정_결과 = 테이블_상태_수정(테이블_ID, 테이블_사용중);

        테이블_상태_수정됨(테이블_상태_수정_결과, 테이블_사용중);

        int 인원수 = 6;
        ExtractableResponse<Response> 테이블_인원_수정_결과 = 테이블_인원_수정(테이블_ID, 인원수);

        테이블_인원_수정됨(테이블_인원_수정_결과, 인원수);
    }
}
