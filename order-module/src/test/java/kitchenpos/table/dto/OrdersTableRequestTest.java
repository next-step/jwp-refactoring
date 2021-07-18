package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 요청 테스트")
class OrdersTableRequestTest {

    @Test
    void 주문_테이블_요청_객체를_이용하여_주문_테이블_entity_생성() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(1L, 0, true);
        OrderTable actual = orderTableRequest.toOrderTable();
        assertThat(actual).isEqualTo(new OrderTable(1L, 0, true));
    }
}
