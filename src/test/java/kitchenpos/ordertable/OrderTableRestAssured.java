package kitchenpos.ordertable;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTableRestAssured {
    public static OrderTable from(Long id, int numberOfGuests, boolean empty) {
        OrderTable OrderTable = new OrderTable();
        OrderTable.setId(id);
        OrderTable.setNumberOfGuests(numberOfGuests);
        OrderTable.setEmpty(empty);
        return OrderTable;
    }

    public static void 테이블_등록_되어_있음(OrderTable OrderTable) {
        테이블_생성_요청(OrderTable);
    }

    public static void 테이블_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    public static void 테이블_생성_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 테이블_정보_수정됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_정보_수정안됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(OrderTable params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_empty_수정_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + id + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님수_수정_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + id + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 테이블_조회_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_조회_목록_포함됨(ExtractableResponse<Response> response, List<Long> expectOrderTableList) {
        List<Long> retrieveOrderTableList = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(retrieveOrderTableList).containsAll(expectOrderTableList);
    }
}
