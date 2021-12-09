package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;

public class TableAcceptanceStep {

    public static OrderTable 테이블_저장되어_있음(int numberOfGuests, boolean empty) {
        return 테이블_생성_요청(numberOfGuests, empty).as(OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(createRequest(numberOfGuests, empty))
            .when()
            .post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 테이블_생성됨(ExtractableResponse<Response> response,
        int expectedNumberOfGuests, boolean expectedEmpty) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.as(OrderTable.class))
                .extracting(
                    OrderTable::getNumberOfGuests, OrderTable::isEmpty, OrderTable::getTableGroupId)
                .containsExactly(expectedNumberOfGuests, expectedEmpty, null)
        );
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 테이블_목록_조회됨(ExtractableResponse<Response> response,
        int expectedNumberOfGuests, boolean expectedEmpty) {
        List<OrderTable> orderTables = response.as(new TypeRef<List<OrderTable>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(orderTables)
                .first()
                .extracting(OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactly(expectedNumberOfGuests, expectedEmpty)
        );
    }

    public static ExtractableResponse<Response> 테이블_빈_상태_수정_요청(long id, boolean empty) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(updateEmptyRequest(empty))
            .when()
            .put("/api/tables/{orderTableId}/empty", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_빈_상태_수정됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderTable.class))
                .extracting(OrderTable::isEmpty)
                .isEqualTo(expectedEmpty)
        );
    }

    public static ExtractableResponse<Response> 테이블_손님_수_수정_요청(long id, int number) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(updateNumberOfGuestsRequest(number))
            .when()
            .put("/api/tables/{orderTableId}/number-of-guests", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_손님_수_수정됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderTable.class))
                .extracting(OrderTable::getNumberOfGuests)
                .isEqualTo(expectedNumberOfGuests)
        );
    }

    private static OrderTable updateEmptyRequest(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private static OrderTable updateNumberOfGuestsRequest(int number) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(number);
        return orderTable;
    }

    private static OrderTable createRequest(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
