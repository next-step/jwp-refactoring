package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceTestFixture {
    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성되어_있음(OrderTable orderTable) {
        return 주문_테이블_생성_요청(orderTable);
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용여부_변경_요청(OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTablesId}/empty", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용객_수_변경_요청(OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{orderTablesId}/number-of-guests", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> responses) {
        List<Long> expectedIds = responses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }

    public static void 주문_테이블_이용여부_변경됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getBoolean("empty")).isEqualTo(expectedEmpty);
    }

    public static void 주문_테이블_이용객_수_변경됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(expectedNumberOfGuests);
    }
}
