package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        when(orderTableDao.save(any())).thenReturn(orderTable);

        //when
        OrderTable table = tableService.create(new OrderTable());

        //then
        assertThat(orderTable.getId()).isEqualTo(table.getId());
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderTable orderTable2 = OrderTable.of(2L, null, 3, false);
        List<OrderTable> list = Arrays.asList(orderTable, orderTable2);
        when(orderTableDao.findAll()).thenReturn(list);

        //when
        List<OrderTable> resultList = tableService.list();

        //then
        assertThat(resultList.size()).isEqualTo(list.size());
    }

    @DisplayName("테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        //when
        OrderTable changeEmptyOrderTable = tableService.changeEmpty(orderTable.getId(), targetOrderTable);

        //then
        assertThat(changeEmptyOrderTable.isEmpty()).isEqualTo(targetOrderTable.isEmpty());
    }

    @DisplayName("그룹테이블에 속해있는 테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty2() {
        //given
        OrderTable orderTable = OrderTable.of(1L, 1L, 2, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 빈테이블로 변경한다.")
    @Test
    void changeEmpty3() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 4, orderTable.isEmpty());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(targetOrderTable);

        //when
        OrderTable changeNumberOfGuestsOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable);

        //then
        assertThat(changeNumberOfGuestsOrderTable.getNumberOfGuests()).isEqualTo(targetOrderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를  -1로 변경한다.")
    @Test
    void changeNumberOfGuests2() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, false);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), -1, orderTable.isEmpty());

        // then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests3() {
        //given
        OrderTable orderTable = OrderTable.of(1L, null, 2, true);
        OrderTable targetOrderTable = OrderTable.of(orderTable.getId(), orderTable.getTableGroupId(), 4, orderTable.isEmpty());
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), targetOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
