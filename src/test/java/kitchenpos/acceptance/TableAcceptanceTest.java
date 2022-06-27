package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.helper.KitchenPosBehaviors;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableAcceptanceTest extends AcceptanceTest {
    /**
     * When 빈 테이블을 생성한다.
     * Then 빈 테이블이 생성된다.
     * When 테이블을 조회한다.
     * Then 테이블이 조회된다.
     * When 테이블의 상태를 변경한다.
     * Then 테이블의 상태가 변경된다.
     */
    @Test
    @DisplayName("테이블 생성, 조회, 상태 변경 기능 인수테스트")
    void tableAcceptanceTest() {
        OrderTable table = OrderTableFixtureFactory.createNotEmptyOrderTable(0);
        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.테이블_생성_요청(table);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<OrderTable> orderTables = KitchenPosBehaviors.테이블_목록조회();
        assertThat(orderTables).hasSize(1);

        OrderTable savedTable = orderTables.get(0);
        Long savedTableId = savedTable.getId();

        빈테이블로_변경하고_확인한다(savedTableId);
        인원수를_변경하고_확인한다(savedTableId, 3);
    }

    private void 빈테이블로_변경하고_확인한다(Long orderTableId) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        ExtractableResponse<Response> changeEmptyStateResponse = KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTableId,
                param);
        assertThat(changeEmptyStateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTable> orderTables = KitchenPosBehaviors.테이블_목록조회();
        OrderTable emptyTable = orderTables.get(0);
        assertThat(emptyTable.isEmpty()).isFalse();
    }

    private void 인원수를_변경하고_확인한다(Long orderTableId, int numberOfGuests) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(numberOfGuests);
        ExtractableResponse<Response> changeEmptyStateResponse = KitchenPosBehaviors.테이블_인원수_변경_요청(orderTableId, param);
        assertThat(changeEmptyStateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTable> orderTables = KitchenPosBehaviors.테이블_목록조회();
        OrderTable changedTable = orderTables.get(0);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}
