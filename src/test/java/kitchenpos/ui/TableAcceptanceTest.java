package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static kitchenpos.utils.TestHelper.등록되어_있지_않은_orderTable_id;
import static kitchenpos.utils.TestHelper.비어있지_않은_orderTable_id;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {
    @Test
    void createTable() {
        OrderTable orderTable = OrderTable.of(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_생성_요청(orderTable);

        주문_테이블_생성됨(response);
    }

    @Test
    void changeEmpty() {
        OrderTable orderTable = OrderTable.of(비어있지_않은_orderTable_id, 0, true);

        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(orderTable);

        주문_테이블_상태_변경됨(response, orderTable);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 상태를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        OrderTable orderTable = OrderTable.of(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청(orderTable);

        주문_테이블_상태_변경_실패(response);
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(비어있지_않은_orderTable_id, 3, false);

        ExtractableResponse<Response> response = 주문_테이블_손님수_변경_요청(orderTable);

        주문_테이블_사람수_변경됨(response, orderTable);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 사람 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsException() {
        OrderTable orderTable = OrderTable.of(등록되어_있지_않은_orderTable_id, 1, false);

        ExtractableResponse<Response> response = 주문_테이블_손님수_변경_요청(orderTable);

        주문_테이블_사람수_변경_실패(response);
    }

    private static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/empty", orderTable.getId())
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all()
                .extract();
    }

    private static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 주문_테이블_상태_변경됨(ExtractableResponse<Response> response, OrderTable orderTable) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    private static void 주문_테이블_상태_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 주문_테이블_사람수_변경됨(ExtractableResponse<Response> response, OrderTable orderTable) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    private static void 주문_테이블_사람수_변경_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
