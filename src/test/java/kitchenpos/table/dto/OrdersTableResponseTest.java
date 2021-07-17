package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 반환 객체 테스트")
class OrdersTableResponseTest {

    @Test
    void 주문_테이블_entity를_이용하여_주문_테이블_반환_객체_생성() {
        OrderTable orderTable = new OrderTable(1L, 0, true);

        OrderTableResponse expected = OrderTableResponse.of(orderTable);
        assertThat(expected.getId()).isEqualTo(orderTable.getId());
        assertThat(expected.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().numberOfGuests());
        assertThat(expected.getTableGroupId()).isEqualTo(orderTable.getTableGroupId());
        assertThat(expected.isEmpty()).isEqualTo(orderTable.isEmpty());
    }
}
