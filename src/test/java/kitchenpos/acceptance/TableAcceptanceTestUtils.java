package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableAcceptanceTestUtils {
    private static final String TABLE_PATH = "/api/tables";

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(TABLE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post(TABLE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_테이블_수정_요청(Long orderTableId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put(TABLE_PATH + "/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_수정_요청(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put(TABLE_PATH + "/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(Long tableGroupId, int numberOfGuests, boolean empty) {
        return 주문_테이블_생성_요청(tableGroupId, numberOfGuests, empty);
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        Long tableGroupId = response.jsonPath()
                .getObject("tableGroupId", Long.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(tableGroupId).isNull();
    }

    public static void 테이블_손님_수_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_손님_수_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 테이블_손님_수_검증(ExtractableResponse<Response> response, int expect) {
        OrderTable changeTable = response.as(OrderTable.class);
        assertThat(changeTable.getNumberOfGuests()).isEqualTo(expect);
    }

    public static void 비어있는_테이블로_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 비어있는_테이블로_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 비어있는_주문_테이블_검증(ExtractableResponse<Response> response) {
        OrderTable changeTable = response.as(OrderTable.class);
        assertTrue(changeTable.isEmpty());
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, OrderTable... orderTables) {
        List<Long> actualIds = response.jsonPath()
                .getList("id", Long.class);
        List<Long> expectIds = Arrays.stream(orderTables)
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsExactlyElementsOf(expectIds);
    }
}