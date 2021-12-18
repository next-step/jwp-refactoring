package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Arrays;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("주문테이블 관련 테스트")
class TableServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderTableDao = mock(OrderTableDao.class);
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("create메서드에 생성을 원하는 OrderTable 객체를 인자로 하여 호출하면, 생성된 객체를 반환한다.")
    @Test
    void createTest() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, true);
        when(orderTableDao.save(any())).thenReturn(orderTable);
        assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
    }

    @DisplayName("list메서드를 호출하면, OrderTable 목록을 반환한다.")
    @Test
    void listTest() {
        OrderTable orderTable1 = OrderTable.of(1L, 1L, 4, true);
        OrderTable orderTable2 = OrderTable.of(2L, 1L, 6, true);

        when(orderTableDao.findAll()).thenReturn(Lists.newArrayList(orderTable1, orderTable2));
        assertThat(tableService.list()).isEqualTo(Lists.newArrayList(orderTable1, orderTable2));
    }

    @DisplayName("changeEmpty메서드를 호출하면, 테이블의 비어 있음 여부를 변경할 수 있다.")
    @Test
    void changeEmptyTest() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), targetOrderTable).isEmpty()).isEqualTo(targetOrderTable.isEmpty());
    }

    @DisplayName("changeEmpty메서드를 호출시, OrderTable객체에 TableGroupId가 할당되어 있다면, 예외를 던진다.")
    @Test
    void exceptionTest1() {
        OrderTable orderTable = OrderTable.of(1L, 1L, 4, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeEmpty(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty메서드를 호출시, OrderTable의 식별자로 조회된 주문이 조리 또는 식사중이면, 예외를 던진다.")
    @Test
    void exceptionTest2() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
            () -> tableService.changeEmpty(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests메서드를 호출하면, 테이블의 인원을 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTable orderTable = OrderTable.of(1L, null, 4, true);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 6, true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), targetOrderTable).getNumberOfGuests()).isEqualTo(
            targetOrderTable.getNumberOfGuests()
        );
    }

    @DisplayName("changeNumberOfGuests메서드를 호출시, 변경하려는 OrderTable객체의 손님수가 음수라면, 예외를 던진다.")
    @Test
    void exceptionTest3() {
        OrderTable orderTable = OrderTable.of(1L, 1L, 4, true);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), -1, true);

        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests메서드를 호출시, 저장된 OrderTable객체가 없다면, 예외를 던진다.")
    @Test
    void exceptionTest4() {
        OrderTable orderTable = OrderTable.of(1L, 1L, 4, true);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 6, true);

        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests메서드를 호출시, OrderTable의 식별자로 조회된 주문이 조리 또는 식사중이면, 예외를 던진다.")
    @Test
    void exceptionTest5() {
        OrderTable orderTable = OrderTable.of(1L, 1L, 4, true);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 6, true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
