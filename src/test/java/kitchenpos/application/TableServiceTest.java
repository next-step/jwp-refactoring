package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.CannotChangeEmptyException;
import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.order.exception.NegativeNumberOfGuestsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(null, 1, false);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void changeEmptyTableTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, true);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));

        // when
        TableService tableService = new TableService(orderTableRepository);
        OrderTableResponse returnedOrderTable = tableService.changeEmpty(1L, orderTableRequest);

        assertThat(returnedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 속해있는 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void changeEmptyTableInTableGroupTest() {
        // given
        orderTable.changeTableGroup(new TableGroup());
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, true);

        // when
        TableService tableService = new TableService(orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .isInstanceOf(CannotChangeEmptyException.class);
    }

    @DisplayName("조리 중이거나 식사 중인 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void changeEmptyTableInCookingOrMeal() {
        // given
        orderTable.addOrder(Order.of(orderTable, OrderStatus.COOKING));
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, true);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));

        // when
        TableService tableService = new TableService(orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .isInstanceOf(CannotChangeEmptyException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 지정한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));

        // when
        TableService tableService = new TableService(orderTableRepository);
        OrderTableResponse returnedOrderTable = tableService.changeNumberOfGuests(1L, orderTableRequest);

        // then
        assertThat(returnedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 지정한다")
    @Test
    void changeNumberOfGuestsNegativeTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));

        // when
        TableService tableService = new TableService(orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(NegativeNumberOfGuestsException.class);
    }

    @DisplayName("빈 테이블의 손님 숫자를 지정할 수 없다")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        orderTable = new OrderTable(null, 1, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(2, true);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));
        // when
        TableService tableService = new TableService(orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(CannotChangeNumberOfGuestsException.class);
    }
}
