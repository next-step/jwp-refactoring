package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.dto.TableChangeEmptyRequest;
import kitchenpos.ordertable.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.TableRequest;
import kitchenpos.ordertable.dto.TableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceTestFixture {
    public static ExtractableResponse<Response> 주문_테이블_생성_요청(TableRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성되어_있음(TableRequest request) {
        return 주문_테이블_생성_요청(request);
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용여부_변경_요청(TableResponse response, boolean expected) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TableChangeEmptyRequest(expected))
                .when().put("/api/tables/{orderTablesId}/empty", response.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_이용객_수_변경_요청(TableResponse response, int numberOfGuests) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TableChangeNumberOfGuestsRequest(numberOfGuests))
                .when().put("/api/tables/{orderTablesId}/number-of-guests", response.getId())
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

        List<Long> actualIds = response.jsonPath().getList(".", TableResponse.class).stream()
                .map(TableResponse::getId)
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
