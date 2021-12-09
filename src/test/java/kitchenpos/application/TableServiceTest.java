package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;


    @DisplayName("테이블을 생성한다.")
    @Test
    void createTest() {

        // given
        OrderTable orderTable = new OrderTable();

        OrderTable expectedOrderTable = mock(OrderTable.class);
        when(expectedOrderTable.getId()).thenReturn(1L);
        when(orderTableDao.save(orderTable)).thenReturn(expectedOrderTable);

        TableService tableService = new TableService(orderDao, orderTableDao);

        // when
        OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertThat(createdOrderTable.getId()).isNotNull();
    }


    @DisplayName("테이블을 조회한다.")
    @Test
    void getListTest() {

        // given
        OrderTable orderTable = new OrderTable();
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));
        TableService tableService = new TableService(orderDao, orderTableDao);

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).contains(orderTable);
    }

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmptyTest() {

        //given
        Long orderTableId = 1L;

        OrderTable orderTable  = mock(OrderTable.class);
        when(orderTable .getTableGroupId()).thenReturn(null);

        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable ));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        OrderTable savedOrderTable = mock(OrderTable.class);
        when(savedOrderTable.isEmpty()).thenReturn(true);
        when(orderTableDao.save(orderTable )).thenReturn(savedOrderTable);

        TableService tableService = new TableService(orderDao, orderTableDao);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests(){
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTable.getNumberOfGuests()).thenReturn(3);
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

        OrderTable savedOrderTable = mock(OrderTable.class);
        when(savedOrderTable.getNumberOfGuests()).thenReturn(3);
        when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);
        TableService tableService = new TableService(orderDao, orderTableDao);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
