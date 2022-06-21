package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 관리")
    void table() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(true, 3);
        OrderTable createdOrderTable = response.as(OrderTable.class);
        // then
        주문_테이블_등록됨(response);

        // when
        response = 주문_테이블_조회_요청();
        // then
        주문_테이블_조회됨(response);

        // when
        response = 주문_테이블_상태_변경_요청(createdOrderTable, false);
        // then
        주문_테이블_상태_변경됨(response, false);

        // when
        response = 주문_테이블_손님수_변경_요청(createdOrderTable, 4);
        // then
        주문_테이블_손님수_변경됨(response, 4);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(boolean empty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);

        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_변경_요청(OrderTable createOrderTable, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", createOrderTable.getId())
                .then().log().all().extract();
    }


    public static ExtractableResponse<Response> 주문_테이블_손님수_변경_요청(OrderTable createOrderTable, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", createOrderTable.getId())
                .then().log().all().extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_테이블_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_상태_변경됨(ExtractableResponse<Response> response, boolean empty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(empty);
    }

    public static void 주문_테이블_손님수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
