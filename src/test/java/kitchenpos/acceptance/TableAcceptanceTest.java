package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문테이블 관련 기능 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = new OrderTable(null, null, 1,  false);
        주문테이블2 = new OrderTable(null, null, 2,  false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문테이블1);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        주문테이블1 = 주문_테이블_생성_요청(주문테이블1).as(OrderTable.class);
        주문테이블2 = 주문_테이블_생성_요청(주문테이블2).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_확인됨(response, Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
    }

    @DisplayName("주문 테이블의 비어있는 상태를 변경한다.")
    @Test
    void updateOrderTableEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(주문테이블1).as(OrderTable.class);
        OrderTable updateOrderTable = new OrderTable(주문테이블1.getId(), 주문테이블1.getTableGroupId(), 주문테이블1.getNumberOfGuests(), !orderTable.isEmpty());

        // when
        ExtractableResponse<Response> response = 주문_테이블_빈_여부_변경_요청(orderTable.getId(), updateOrderTable);

        // then
        주문_테이블_빈_여부_변경됨(response, updateOrderTable.isEmpty());
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    @Test
    void updateOrderTableNumberOfGuests() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(주문테이블1).as(OrderTable.class);
        OrderTable updateOrderTable = new OrderTable(주문테이블1.getId(), 주문테이블1.getTableGroupId(), 10, orderTable.isEmpty());

        // when
        ExtractableResponse<Response> response = 주문_테이블_방문_손님_수_변경_요청(orderTable.getId(), updateOrderTable);

        // then
        주문_테이블_손님_수_변경됨(response, updateOrderTable.getNumberOfGuests());
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 주문_테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_테이블_목록_확인됨(ExtractableResponse<Response> response, List<Long> orderTableIds) {
        List<Long> resultIds = response.jsonPath().getList(".", OrderTable.class)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(resultIds).containsAll(orderTableIds);
    }

    private void 주문_테이블_빈_여부_변경됨(ExtractableResponse<Response> response, boolean expect) {
        boolean result = response.jsonPath().getBoolean("empty");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expect)
        );
    }

    private void 주문_테이블_손님_수_변경됨(ExtractableResponse<Response> response, int expect) {
        int result = response.jsonPath().getInt("numberOfGuests");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result).isEqualTo(expect)
        );
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_여부_변경_요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_방문_손님_수_변경_요청(Long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

}
