package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관련 기능 테스트")
public class TableAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("테이블 관리")
    void table() {
        // when
        ExtractableResponse<Response> initResponse = 주문_테이블_조회_요청();
        assertThat(initResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(true, 0);
        OrderTable createOrderTable = response.as(OrderTable.class);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> getResponse = 주문_테이블_조회_요청();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getList(".")).hasSize(initResponse.jsonPath().getList(".").size() + 1);

        // when
        ExtractableResponse<Response> changeEmptyResponse = 테이블_상태_변경(createOrderTable, false);

        // then
        assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeEmptyResponse.as(OrderTable.class).isEmpty()).isFalse();

        // when
        ExtractableResponse<Response> changeNumberOfGuestResponse = 테이블_손님수_변경(createOrderTable, 4);

        // then
        assertThat(changeNumberOfGuestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changeNumberOfGuestResponse.as(OrderTable.class).getNumberOfGuests()).isEqualTo(4);
    }

    private ExtractableResponse<Response> 테이블_손님수_변경(OrderTable createOrderTable, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeNumberOfGuests(numberOfGuests);
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", createOrderTable.getId())
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 테이블_상태_변경(OrderTable createOrderTable, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(empty);
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", createOrderTable.getId())
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(boolean empty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(empty);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all().extract();
    }
}
