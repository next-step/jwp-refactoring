package kitchenpos.table.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/tables";

    @Test
    void manageTable() {
        // 빈 테이블 등록 요청
        ExtractableResponse<Response> saveResponse = 빈_테이블_등록_요청();
        // 빈 테이블 등록 됨
        빈_테이블_등록됨(saveResponse);

        OrderTable savedOrderTable = saveResponse.as(OrderTable.class);
        // 테이블 목록 조회 요청
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();
        // 테이블 목록 조회됨
        테이블_목록_조회됨(response, Arrays.asList(savedOrderTable));

        // 빈테이블 -> 주문 테이블 상태 변경 요청
        ExtractableResponse<Response> emptyResponse = 테이블_상태변경_요청(savedOrderTable.getId(), false);

        // 상태 변경됨
        테이블_상태변경_됨(emptyResponse, false);

        // 테이블의 손님 수 변경 요청
        ExtractableResponse<Response> numberResponse = 테이블_손님수_변경_요청(savedOrderTable.getId(), 3);

        // 테이블의 손님 수 변경 됨
        테이블_손님수_변경_됨(numberResponse, 3);

        // 테이블 목록 조회 요청
        response = 테이블_목록_조회_요청();
        // 테이블 목록 조회됨
        테이블_목록_조회됨(response, Arrays.asList(numberResponse.as(OrderTable.class)));

    }

    public static ExtractableResponse<Response> 테이블_등록_요청(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(orderTable)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 빈_테이블_등록_요청() {
        return 테이블_등록_요청(0, true);
    }

    public static void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 빈_테이블_등록됨(ExtractableResponse<Response> response) {
        OrderTable orderTable = response.as(OrderTable.class);

        assertAll(() -> {
            테이블_등록됨(response);
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(0);
            assertTrue(orderTable.isEmpty());
        });
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 테이블_목록_조회됨(ExtractableResponse<Response> response, List<OrderTable> expected) {
        List<OrderTable> orderTables = response.jsonPath().getList(".", OrderTable.class);
        List<Long> expectedIds = expected.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTables)
                .extracting(OrderTable::getId)
                .containsAll(expectedIds);
        });
    }

    public static ExtractableResponse<Response> 테이블_상태변경_요청(Long tableId, boolean isEmpty) {
        OrderTable request = new OrderTable();
        request.setEmpty(isEmpty);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(URL+"/{orderTableId}/empty", tableId)
            .then().log().all()
            .extract();
    }

    public static void 테이블_상태변경_됨(ExtractableResponse<Response> response, boolean expected) {
        OrderTable orderTable = response.as(OrderTable.class);

        assertAll(() ->{
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTable.isEmpty()).isEqualTo(expected);
        });
    }

    public static ExtractableResponse<Response> 테이블_손님수_변경_요청(Long tableId, int numberOfGuests) {
        OrderTable request = new OrderTable();
        request.setNumberOfGuests(numberOfGuests);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().put(URL+"/{orderTableId}/number-of-guests", tableId)
            .then().log().all()
            .extract();
    }

    public static void 테이블_손님수_변경_됨(ExtractableResponse<Response> response, int expected) {
        OrderTable orderTable = response.as(OrderTable.class);

        assertAll(() ->{
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
        });
    }

    public static OrderTable 빈테이블_등록됨() {
        return 빈_테이블_등록_요청().as(OrderTable.class);
    }

}
