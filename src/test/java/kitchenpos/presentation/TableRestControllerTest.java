package kitchenpos.presentation;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("주문테이블 API기능에 관한")
public class TableRestControllerTest extends TestConfig {
    @DisplayName("주문테이블이 저장된다.")
    @Test
    void save_table() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);

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
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(0);

        // when
        OrderTable savedOrderTable = TableRestControllerTest.주문테이블_저장요청(orderTable).as(OrderTable.class);
      
        // given
        savedOrderTable.setEmpty(true);

        // when
        ExtractableResponse<Response> response = 주문테이블_빈테이블_변경요청(savedOrderTable);

        // then
        주문테이블_빈테이블_변경됨(response);
    }

    @DisplayName("주문테이블의 고객수가 변경된다.")
    @Test
    void update_tableForNumberOfGuests() {
        // given
        List<OrderTable> orderTables = 빈테이블_조회();

        // when
        OrderTable orderTable = orderTables.get(0);
        orderTable.setEmpty(false);
        OrderTable orderTableChangedStatus = 주문테이블_빈테이블_변경요청(orderTable).as(OrderTable.class);

        // when
        orderTableChangedStatus.setNumberOfGuests(2);
        ExtractableResponse<Response> response = 주문테이블_고객수_변경요청(orderTableChangedStatus);

        // then
        주문테이블_고객수_변경됨(response);
    }

    private List<OrderTable> 빈테이블_조회() {
        return List.of(주문테이블_조회요청().as(OrderTable[].class)).stream()
                    .filter(OrderTable::isEmpty)
                    .collect(Collectors.toList());
    }

    private void 주문테이블_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(2);
        Assertions.assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(false);
    }

    public static ExtractableResponse<Response> 주문테이블_저장요청(OrderTable orderTable) {
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
            () -> Assertions.assertThat(response.as(OrderTable.class).isEmpty()).isTrue()
        );
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블_변경요청(OrderTable orderTable) {
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
            () -> Assertions.assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(2)  
        );
    }

    public static ExtractableResponse<Response> 주문테이블_고객수_변경요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + orderTable.getId() + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
