package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

public class TableAcceptanceTestHelper {
    public static ExtractableResponse<Response> 좌석_등록_되어_있음(boolean empty, int numberOfGuests, Long tableGroupId) {
        return 주문_테이블_생성_요청(empty, numberOfGuests, tableGroupId);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(boolean empty, int numberOfGuests, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(Long id, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/number-of-guests", id)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_정리_요청(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/empty", id)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all().extract();
    }

    public static void 주문_테이블_정리됨(ExtractableResponse<Response> response) {
        OrderTable orderTable = response.as(OrderTable.class);

        assertThat(orderTable.isEmpty()).isTrue();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_테이블_손님_수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTable orderTable = response.as(OrderTable.class);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }
}
