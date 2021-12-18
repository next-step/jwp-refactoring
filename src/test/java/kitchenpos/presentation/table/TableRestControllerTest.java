package kitchenpos.presentation.table;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("주문테이블 API기능에 관한")
public class TableRestControllerTest extends TestConfig {
    @DisplayName("주문테이블이 저장된다.")
    @Test
    void save_table() {
        // given
        OrderTableDto orderTable = OrderTableDto.of(2);

        // when
        ExtractableResponse<Response> response = 주문테이블_저장요청(orderTable);

        // then
        주문테이블_저장됨(response);
    }

    @DisplayName("주문테이블이 조회된다.")
    @Test
    void search_table() {
        // when
        ExtractableResponse<Response> response = 주문테이블_조회요청();

        // then
        주문테이블_조회됨(response);
    }

    @DisplayName("주문테이블이 빈테이블에서 해제된다.")
    @Test
    void update_table() {
        // given
        OrderTableDto orderTable = OrderTableDto.of(10);

        // when
        OrderTableDto savedOrderTable = TableRestControllerTest.주문테이블_저장요청(orderTable).as(OrderTableDto.class);

        // given
        savedOrderTable.changeNumberOfGuests(0);
        savedOrderTable.changeEmpty(true);

        // when
        ExtractableResponse<Response> response = 주문테이블_빈테이블_변경요청(savedOrderTable);

        // then
        주문테이블_빈테이블_변경됨(response);
    }

    @DisplayName("주문테이블의 고객수가 변경된다.")
    @Test
    void update_tableForNumberOfGuests() {
        // given
        OrderTableDto orderTable = OrderTableDto.of(10);

        // when
        OrderTableDto savedOrderTable = TableRestControllerTest.주문테이블_저장요청(orderTable).as(OrderTableDto.class);
        savedOrderTable.changeNumberOfGuests(2);
        ExtractableResponse<Response> response = 주문테이블_고객수_변경요청(savedOrderTable);

        // then
        주문테이블_고객수_변경됨(response);
    }

    private void 주문테이블_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.as(OrderTableDto.class).getNumberOfGuests()).isEqualTo(2);
        Assertions.assertThat(response.as(OrderTableDto.class).isEmpty()).isEqualTo(false);
    }

    public static ExtractableResponse<Response> 주문테이블_저장요청(OrderTableDto orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private void 주문테이블_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 주문테이블_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private void 주문테이블_빈테이블_변경됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> Assertions.assertThat(response.as(OrderTableDto.class).isEmpty()).isTrue()
        );
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블_변경요청(OrderTableDto orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + orderTable.getId() + "/empty")
                .then().log().all()
                .extract();
    }

    private void 주문테이블_고객수_변경됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> Assertions.assertThat(response.as(OrderTableDto.class).getNumberOfGuests()).isEqualTo(2)
        );
    }

    public static ExtractableResponse<Response> 주문테이블_고객수_변경요청(OrderTableDto orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
