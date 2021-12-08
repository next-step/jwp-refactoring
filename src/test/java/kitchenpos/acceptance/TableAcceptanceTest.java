package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable orderTable;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
    }

    @Test
    @DisplayName("테이블를 등록한다.")
    void create() {
        // when
        ExtractableResponse<Response> response = 테이블_등록_요청(orderTable);

        // then
        테이블_등록됨(response);
    }

    @Test
    @DisplayName("테이블의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();

        // then
        테이블_목록_조회됨(response);
    }

    @Test
    @DisplayName("테이블의 주문 등록 가능 여부를 변경한다.")
    void changeEmpty() {
        // given
        OrderTable savedOrderTable = 테이블_등록되어_있음(orderTable);

        OrderTable modifyOrderTable = new OrderTable();
        modifyOrderTable.setEmpty(false);

        // when
        ExtractableResponse<Response> response = 테이블_주문_등록_가능_여부_변경_요청(savedOrderTable.getId(), modifyOrderTable);

        // then
        테이블_주문_등록_가능_여부_변경됨(response);
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable 등록된_테이블 = 테이블_등록되어_있음(orderTable);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        // when
        ExtractableResponse<Response> response = 테이블_방문한_손님_수_변경_요청(등록된_테이블.getId(), orderTable);

        // then
        테이블_방문한_손님_수_변경됨(response);
    }

    public static OrderTable 테이블_등록되어_있음(OrderTable orderTable) {
        return 테이블_등록_요청(orderTable).as(OrderTable.class);
    }

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTable orderTable) {
        return post("/api/tables", orderTable);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get("/api/tables");
    }

    public static ExtractableResponse<Response> 테이블_주문_등록_가능_여부_변경_요청(Long orderTableId, OrderTable orderTable) {
        return put("/api/tables/{orderTableId}/empty", orderTable, orderTableId);
    }

    private ExtractableResponse<Response> 테이블_방문한_손님_수_변경_요청(long orderTableId, OrderTable orderTable) {
        return put("/api/tables/{orderTableId}/number-of-guests", orderTable, orderTableId);
    }

    private void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", OrderTable.class).size()).isPositive();
    }

    private void 테이블_주문_등록_가능_여부_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 테이블_방문한_손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
