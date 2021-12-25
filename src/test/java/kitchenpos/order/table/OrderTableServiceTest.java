package kitchenpos.order.table;

import kitchenpos.order.table.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.table.domain.OrderTable;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("주문 테이블 생성하기")
    @Test
    void createTable() {

        //given
        OrderTable orderTable = new OrderTable();
        when(orderTableDao.save(any())).thenReturn(orderTable);

        //when
        OrderTable savedOrderTable = tableService.create(orderTable);

        //then
        assertThat(savedOrderTable).isNotNull();
        assertThat(savedOrderTable.getTableGroup()).isNull();
    }
    
    @DisplayName("주문 테이블 조회하기")
    @Test
    void getTables() {

        //given
        OrderTable orderTableA = new OrderTable();
        OrderTable orderTableB = new OrderTable();
        List<OrderTable> orderTables = Arrays.asList(orderTableA, orderTableB);
        when(orderTableDao.findAll()).thenReturn(orderTables);

        //when
        List<OrderTable> findOrderTables = tableService.list();

        //then
        assertThat(findOrderTables).isNotEmpty();
        assertThat(findOrderTables).contains(orderTableA, orderTableB);
    }

    @DisplayName("주문 테이블 비우기")
    @Test
    void emptyTable() {

        //given
        final boolean tableOrderStatus = false;
        OrderTable originOrderTable = new OrderTable();
        originOrderTable.setId(1L);
//        originOrderTable.setTableGroupId(null);
        originOrderTable.setEmpty(false);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setId(1L);
//        newOrderTable.setTableGroupId(null);
        newOrderTable.setEmpty(true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(tableOrderStatus);
        when(orderTableDao.save(any())).thenReturn(newOrderTable);

        //when
        OrderTable emptyOrderTable = tableService.changeEmpty(originOrderTable.getId(), originOrderTable);

        //then
        assertThat(emptyOrderTable).isNotNull();
        assertThat(emptyOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블 비울 시 table group id가 존재할 경우")
    @Test
    void emptyTableByExistedTableGroupId() {

        //given
        OrderTable originOrderTable = new OrderTable();
        originOrderTable.setId(1L);
//        originOrderTable.setTableGroupId(1L);
        originOrderTable.setEmpty(false);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(originOrderTable.getId(), originOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비울 시 주문상태가 Cooking이거나 Meal 단계가 있을경우")
    @Test
    void emptyTableByOrderStatusCookingAndMeal() {

        //given
        final boolean existTableOrderStatus = true;
        OrderTable originOrderTable = new OrderTable();
        originOrderTable.setId(1L);
//        originOrderTable.setTableGroupId(null);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(originOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(existTableOrderStatus);

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(originOrderTable.getId(), originOrderTable))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블 손님 수 조정하기")
    @Test
    void changeGuestNumberOfTable() {

        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(7);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        //when
        OrderTable changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        //then
        assertThat(changeOrderTable).isNotNull();
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("주문 테이블 손님 수 조정할 때 조정하려는 손님 수가 0일경우")
    @Test
    void changeGuestNumberOfTableByNumberNegative() {

        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(-1);

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님 수 조정할 때 조정하려는 손님 수가 0일경우")
    @Test
    void changeGuestNumberOfTableByEmptyTable() {

        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.ofNullable(orderTable));

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
