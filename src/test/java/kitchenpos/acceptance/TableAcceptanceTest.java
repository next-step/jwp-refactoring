package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/tables";

    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manageMenuGroup() {
        // given
        OrderTable 비어있는_주문_테이블 = new OrderTable();
        비어있는_주문_테이블.setNumberOfGuests(0);
        비어있는_주문_테이블.setEmpty(true);
        // when
        ExtractableResponse<Response> 주문_테이블_생성_응답 = 주문_테이블_생성_요청(비어있는_주문_테이블);
        // then
        주문_테이블_생성됨(주문_테이블_생성_응답);

        // when
        ExtractableResponse<Response> 주문_테이블_목록_조회_응답 = 주문_테이블_목록_조회_요청();
        // then
        주문_테이블_목록_조회됨(주문_테이블_목록_조회_응답);

        // given
        boolean empty = false;
        // when
        Long 주문_테이블_ID = 주문_테이블_ID_조회(주문_테이블_생성_응답);
        ExtractableResponse<Response> 주문_테이블_수정_응답 = 주문_테이블_수정(주문_테이블_ID, empty);
        // then
        주문_테이블_수정됨(주문_테이블_수정_응답, empty);

        // given
        int numberOfGuests = 4;
        // when
        ExtractableResponse<Response> 주문_테이블_손님_수_수정_응답 = 주문_테이블_손님_수_수정(주문_테이블_ID, numberOfGuests);
        // then
        주문_테이블_손님_수_수정됨(주문_테이블_손님_수_수정_응답, numberOfGuests);
    }

    private ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Long 주문_테이블_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 주문_테이블_수정(Long orderTableId, boolean empty) {
        OrderTable params = new OrderTable();
        params.setEmpty(empty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(String.format("%s/{orderTableId}/empty", API_URL), orderTableId)
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_수정됨(ExtractableResponse<Response> response, boolean expect) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertThat(response.jsonPath().getBoolean("empty")).isEqualTo(expect)
        );
    }

    private ExtractableResponse<Response> 주문_테이블_손님_수_수정(Long orderTableId, int numberOfGuests) {
        OrderTable params = new OrderTable();
        params.setNumberOfGuests(numberOfGuests);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(String.format("%s/{orderTableId}/number-of-guests", API_URL), orderTableId)
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_손님_수_수정됨(ExtractableResponse<Response> response, int expect) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
                , () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(expect)
        );
    }
}
