package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {
    private static final String TABLE_URL = "/api/tables";
    public OrderTable 구번_테이블;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        구번_테이블 = new OrderTable();
        구번_테이블.setEmpty(true);
        구번_테이블.setNumberOfGuests(0);
    }

    /**
     * when 테이블을 추가 한다
     * then 테이블이 추가됨.
     * <p>
     * when 전체 테이블을 조회 한다.
     * then 추가된 테이블이 조회 됨.
     *
     * when 테이블 상태 값을 변경 한다.
     * then 테이블의 상태 값이 변경 된다.
     *
     * when 테이블의 손님을 추가 한다.
     * then 해당 테이블의 손님 수가 수정 된다.
     *
     * when 변경된 테이블을 조회 한다.
     * then 변경된 테이블의 상태와 손님 수를 확인 한다.
     */
    @Test
    @DisplayName("테이블 관리 테스트")
    void table() {
        // when
        final ExtractableResponse<Response> 테이블_추가_요청 = 테이블_추가_요청(구번_테이블);
        // then
        final OrderTable table = 테이블_추가_됨(테이블_추가_요청);

        // when
        final ExtractableResponse<Response> 테이블_전체_조회 = 테이블_전체_조회();
        // then
        List<OrderTable> orderTables = 테이블_조회_됨(테이블_전체_조회);
        assertThat(orderTables).contains(table);

        // given
        구번_테이블.setEmpty(false);
        // when
        final ExtractableResponse<Response> 테이블_상태_변경_요청 = 테이블_상태_변경_요청(9L, 구번_테이블);
        // then
        테이블_상태_변경됨(테이블_상태_변경_요청);

        // given
        구번_테이블.setNumberOfGuests(4);
        // when
        final ExtractableResponse<Response> 손님_수_변경_요청 = 손님_수_변경_요청(9L, 구번_테이블);
        // then
        손님_수_변경됨(손님_수_변경_요청);

        // when
        final ExtractableResponse<Response> 변경후_테이블_전체_조회 = 테이블_전체_조회();
        // then
        final List<OrderTable> updateTables = 테이블_조회_됨(변경후_테이블_전체_조회);
        final OrderTable orderTable = updateTables.stream()
                .filter(it -> it.getId() == 9)
                .findFirst()
                .get();

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
        assertThat(orderTable.isEmpty()).isFalse();
    }

    public static ExtractableResponse<Response> 테이블_추가_요청(final OrderTable table) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(table)
                .when().post(TABLE_URL)
                .then().log().all()
                .extract();
    }

    public static OrderTable 테이블_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(OrderTable.class);
    }

    public static OrderTable 테이블_추가_되어_있음(final OrderTable table) {
        return 테이블_추가_됨(테이블_추가_요청(table));
    }

    public static ExtractableResponse<Response> 테이블_전체_조회() {
        return RestAssured.given()
                .when().get(TABLE_URL)
                .then()
                .extract();
    }

    public static List<OrderTable> 테이블_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(final Long tableId, final OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put(TABLE_URL + "/{tableId}/empty", tableId)
                .then().log().all()
                .extract();
    }

    public static void 테이블_상태_변경됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 손님_수_변경_요청(final Long tableId, final OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put(TABLE_URL + "/{tableId}/number-of-guests", tableId)
                .then().log().all()
                .extract();
    }

    public static void 손님_수_변경됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_사용됨(final Long tableId, final OrderTable orderTable) {
        테이블_상태_변경됨(테이블_상태_변경_요청(tableId, orderTable));
        손님_수_변경됨(손님_수_변경_요청(tableId, orderTable));
        final List<OrderTable> updateTables = 테이블_조회_됨(테이블_전체_조회());
        final OrderTable updateTable = updateTables.stream()
                .filter(it -> it.getId() == orderTable.getId())
                .findFirst()
                .get();

        assertThat(updateTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(updateTable.isEmpty()).isFalse();

    }
}
