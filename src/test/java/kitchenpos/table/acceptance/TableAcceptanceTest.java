package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블을 관리한다.")
public class TableAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createTable() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(4, true);

        // then
        주문_테이블_생성됨(response);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void findALl() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회_요청됨(response);
    }

    @Test
    @DisplayName("주문 테이블의 빈 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성_요청(4, true).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(orderTableResponse.getId(), false);

        // then
        주문_테이블_상태_변경_처리됨(response);
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    void changeGuest() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성_요청(4, false).as(OrderTableResponse.class);
        int ten = 10;

        // when
        ExtractableResponse<Response> response = 주문_테이블_손님수_변경_요청(orderTableResponse.getId(), ten);

        // then
        주문_테이블_손님수_변경_처리됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableRequest)
            .when().post("/api/tables")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when().get("/api/tables")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(Long orderTableId, boolean empty) {
        OrderTableStatusRequest orderTableStatusRequest = new OrderTableStatusRequest(empty);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableStatusRequest)
            .when().put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(Long orderTableId, int numberOfGuests) {
        OrderTableGuestRequest orderTableGuestRequest = new OrderTableGuestRequest(numberOfGuests);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTableGuestRequest)
            .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all().extract();
    }

    public static void 주문_테이블_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문_테이블_목록_조회_요청됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_상태_변경_처리됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_손님수_변경_처리됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
