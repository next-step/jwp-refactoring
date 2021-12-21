package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
         orderTable = new OrderTable(1L, new TableGroup(), 1, false);
    }

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void createTest() {
        // given
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);
        OrderTable retunredOrderTable = tableService.create(orderTable);

        // then
        assertThat(retunredOrderTable).isEqualTo(orderTable);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void changeEmptyTableTest() {
        // given
        orderTable.setTableGroup(null);
        orderTable.setEmpty(true);
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);
        OrderTable retunredOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(retunredOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 속해있는 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void changeEmptyTableInTableGroupTest() {
        // given
        orderTable.setEmpty(true);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));

        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리 중이거나 식사 중인 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void changeEmptyTableInCookingOrMeal() {
        // given
        orderTable.setEmpty(true);
        orderTable.setTableGroup(null);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 지정한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        orderTable.setNumberOfGuests(10);
        when(orderTableRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderTable));
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);
        OrderTable returnedOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(returnedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 지정한다")
    @Test
    void changeNumberOfGuestsNegativeTest() {
        // given
        orderTable.setNumberOfGuests(-1);
        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, this.orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 숫자를 지정할 수 없다")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(10);
        // when
        TableService tableService = new TableService(orderRepository, orderTableRepository);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, this.orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void listTest() {
        // given
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
        when(orderTableRepository.findAll()).thenReturn(Collections.singletonList(orderTable));
        TableService tableService = new TableService(orderRepository, orderTableRepository);
        tableService.create(orderTable);

        // when
        List<OrderTable> list = tableService.list();

        // then
        assertThat(list).contains(orderTable);
    }
}
