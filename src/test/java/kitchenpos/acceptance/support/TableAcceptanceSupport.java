package kitchenpos.acceptance.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceSupport {

    public static ExtractableResponse<Response> 주문_테이블_등록요청(OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTable> result = response.jsonPath().getList(".", OrderTable.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(size);
    }

    public static ExtractableResponse<Response> 주문_테이블_빈테이블로_변경요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_빈테이블로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertTrue(result.isEmpty());
    }

    public static ExtractableResponse<Response> 주문_테이블_손님수_변경요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 주문_테이블_손님수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        OrderTable result = response.as(OrderTable.class);
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
