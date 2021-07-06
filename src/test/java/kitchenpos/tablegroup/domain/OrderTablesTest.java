package kitchenpos.tablegroup.domain;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 일급 컬렉션 테스트")
class OrderTablesTest {

    @DisplayName("생성")
    @Test
    void create() {
        // given
        List<OrderTable> orderTablesList = Arrays.asList(new OrderTable(1L, new NumberOfGuests(1), true), new OrderTable(2L, new NumberOfGuests(1), true));
        List<Long> ids = Arrays.asList(1L, 2L);
        // when
        OrderTables orderTable = OrderTables.of(orderTablesList);
        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("생성 실패 - OrderTable이 2개 미만")
    @Test
    void createFailedByOrderTable() {
        // given
        List<OrderTable> orderTablesList = Arrays.asList(new OrderTable());
        List<Long> ids = Arrays.asList(1L, 2L);
        // when
        // then
        assertThatThrownBy(() -> OrderTables.of(orderTablesList))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.INVALID_REQUEST_ORDER_TABLE_SIZE.message());
    }

    @DisplayName("생성 실패 - OrderTable이 비어있지 않음")
    @Test
    void createFailedByOrderTableEmpty() {
        // given
        List<OrderTable> orderTablesList = Arrays.asList(OrderTable.of(new NumberOfGuests(2), false), new OrderTable());
        List<Long> ids = Arrays.asList(1L, 2L);
        // when
        // then
        assertThatThrownBy(() -> OrderTables.of(orderTablesList))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorInfo.ORDER_TABLE_IS_NOT_EMPTY.message());
    }
}