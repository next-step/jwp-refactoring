package kichenpos.table.table.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kichenpos.table.table.ui.request.EmptyRequest;
import kichenpos.table.table.ui.request.OrderTableRequest;
import kichenpos.table.table.ui.request.TableGuestsCountRequest;
import kichenpos.table.table.ui.response.OrderTableResponse;
import org.springframework.http.HttpStatus;

public class TableAcceptanceStep {

    public static OrderTableResponse 테이블_저장되어_있음(int numberOfGuests, boolean empty) {
        return 테이블_생성_요청(numberOfGuests, empty).as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new OrderTableRequest(numberOfGuests, empty))
            .when()
            .post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 테이블_생성됨(ExtractableResponse<Response> response,
        int expectedNumberOfGuests, boolean expectedEmpty) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.as(OrderTableResponse.class))
                .extracting(
                    OrderTableResponse::getNumberOfGuests,
                    OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId)
                .containsExactly(expectedNumberOfGuests, expectedEmpty, null)
        );
    }

    public static ExtractableResponse<Response> 테이블_조회_요청(long id) {
        return RestAssured.given().log().all()
            .when()
            .get("/api/tables/{id}", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_조회됨(ExtractableResponse<Response> response,
        int expectedNumberOfGuests, boolean expectedEmpty) {
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(orderTableResponse)
                .extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
                .containsExactly(expectedNumberOfGuests, expectedEmpty)
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
        List<OrderTableResponse> orderTables = response.as(new TypeRef<List<OrderTableResponse>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(orderTables)
                .first()
                .extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
                .containsExactly(expectedNumberOfGuests, expectedEmpty)
        );
    }

    public static ExtractableResponse<Response> 테이블_빈_상태_수정_요청(long id, boolean empty) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new EmptyRequest(empty))
            .when()
            .put("/api/tables/{orderTableId}/empty", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_빈_상태_수정됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderTableResponse.class))
                .extracting(OrderTableResponse::isEmpty)
                .isEqualTo(expectedEmpty)
        );
    }

    public static ExtractableResponse<Response> 테이블_손님_수_수정_요청(long id, int number) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new TableGuestsCountRequest(number))
            .when()
            .put("/api/tables/{orderTableId}/number-of-guests", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_손님_수_수정됨(ExtractableResponse<Response> response,
        int expectedNumberOfGuests) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderTableResponse.class))
                .extracting(OrderTableResponse::getNumberOfGuests)
                .isEqualTo(expectedNumberOfGuests)
        );
    }

    public static ExtractableResponse<Response> 테이블_주문_받은_상태_수정_요청(long id) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when()
            .post("/api/tables/{orderTableId}/order", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_주문_받은_상태_되어_있음(long id) {
        테이블_주문_받은_상태_수정_요청(id);
    }

    public static ExtractableResponse<Response> 테이블_완료된_상태_수정_요청(long id) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when()
            .post("/api/tables/{orderTableId}/finish", id)
            .then().log().all()
            .extract();
    }

    public static void 테이블_상태_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
