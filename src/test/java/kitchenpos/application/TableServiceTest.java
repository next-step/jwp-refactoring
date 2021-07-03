package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;


@DisplayName("테이블 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setup() {
        tableService = new TableService(orderDao, orderTableDao);
        orderTable = new OrderTable();
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);
        OrderTable orderTable = tableService.create(this.orderTable);
        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("조회")
    @Test
    void list() {
        // given

        // when
        when(orderTableDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(orderTable)));
        List<OrderTable> list = tableService.list();
        // then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }

    @DisplayName("상태 변경")
    @Test
    void changeEmpty() {
        // given
        orderTable.setTableGroupId(null);

        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);
        OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTable);
        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("고객수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        orderTable.setNumberOfGuests(2);
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(1L, this.orderTable);
        // then
        assertThat(changedOrderTable).isNotNull();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("상태 변경 실패 - 테이블 그룹이 없음")
    @Test
    void changeEmptyFailedByTableGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태 변경 실패 - 테이블 그룹id가 null이 아님")
    @Test
    void changeEmptyFailedByTableGroupId() {
        // given
        orderTable.setTableGroupId(1L);
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태 변경 실패 - 주문이 요리중")
    @Test
    void changeEmptyFailedByOrderStatus() {
        // given
        orderTable.setTableGroupId(null);

        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        doReturn(true).when(orderDao).existsByOrderTableIdAndOrderStatusIn(any(), any());
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("고객 수 변경 실패 - 고객이 음수")
    @Test
    void changeNumberOfGuestFailedByNumberOfGuests() {
        // given
        orderTable.setNumberOfGuests(-2);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("고객 수 변경 실패 - 주문 테이블이 없음")
    @Test
    void changeNumberOfGuestFailedByOrderTable() {
        // given
        orderTable.setNumberOfGuests(2);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("고객 수 변경 실패 - 주문 테이블이 비어있음")
    @Test
    void changeNumberOfGuestFailedByOrderTableEmpty() {
        // given
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(true);
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}