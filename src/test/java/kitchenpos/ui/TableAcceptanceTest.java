package kitchenpos.ui;

import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static kitchenpos.utils.AcceptanceTestUtil.put;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("테이블을 생성한다")
    @Test
    void createTable() {
        // when
        ExtractableResponse<Response> 주문테이블_생성_응답 = 주문테이블_생성(OrderTable.ofWithEmpty(4));

        // then
        주문테이블_생성됨(주문테이블_생성_응답);
    }

    @DisplayName("테이블 목록을 조회한다")
    @Test
    void readTables() {
        // given
        OrderTable 주문테이블1 = 주문테이블_생성(OrderTable.ofWithEmpty(4)).as(OrderTable.class);
        OrderTable 주문테이블2 = 주문테이블_생성(OrderTable.ofWithEmpty(3)).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 테이블목록_조회_응답 = 테이블목록_조회();

        // then
        주문테이블_목록_조회됨(테이블목록_조회_응답);
        주문테이블_두_개가_포함됨(테이블목록_조회_응답, 주문테이블1, 주문테이블2);
    }

    @DisplayName("테이블을 빈 상태로 변경한다")
    @Test
    void changeEmpty() {
        // given
        OrderTable 주문테이블 = 주문테이블_생성(OrderTable.of(4)).as(OrderTable.class);
        OrderTable 빈_상태로_변경됨_주문테이블 = new OrderTable(주문테이블.getId(), null, 4, true);

        // when
        ExtractableResponse<Response> 테이블을_빈_상태로_변경_응답 = 테이블을_빈_상태로_변경(빈_상태로_변경됨_주문테이블);

        // then
        테이블_상태가_변경됨(테이블을_빈_상태로_변경_응답);
    }

    @DisplayName("테이블의 인원수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 인원수_네_명_테이블 = 주문테이블_생성(OrderTable.of(4)).as(OrderTable.class);
        OrderTable 인원수_두_명으로_변경된_테이블 = new OrderTable(인원수_네_명_테이블.getId(), null, 2, false);

        // when
        ExtractableResponse<Response> 테이블_인원수_변경_응답 = 테이블_인원수_변경(인원수_두_명으로_변경된_테이블);

        // then
        테이블_인원수가_두_명으로_변경됨(테이블_인원수_변경_응답);

    }

    public static ExtractableResponse<Response> 주문테이블_생성(OrderTable orderTable) {
        return post("/api/tables", orderTable);
    }

    private void 주문테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 테이블목록_조회() {
        return get("/api/tables");
    }

    private void 주문테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문테이블_두_개가_포함됨(ExtractableResponse<Response> 테이블목록_조회_응답, OrderTable orderTable1,
        OrderTable orderTable2) {
        List<OrderTable> orderTables = Lists.newArrayList(테이블목록_조회_응답.as(OrderTable[].class));
        assertThat(orderTables).extracting(OrderTable::getId)
            .contains(orderTable1.getId(), orderTable2.getId());
    }

    private ExtractableResponse<Response> 테이블을_빈_상태로_변경(OrderTable orderTable) {
        String url = String.format("/api/tables/%s/empty", orderTable.getId());
        return put(url, orderTable);
    }

    private void 테이블_상태가_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTable.class).isEmpty()).isTrue();
    }

    private ExtractableResponse<Response> 테이블_인원수_변경(OrderTable orderTable) {
        String url = String.format("/api/tables/%s/number-of-guests", orderTable.getId());
        return put(url, orderTable);
    }

    private void 테이블_인원수가_두_명으로_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(2);
    }

}