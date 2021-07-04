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

    @DisplayName("사용자는 테이블을 생성(예약) 할 수 있다.")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.save(orderTable)).thenReturn(orderTable);
        OrderTable orderTable = tableService.create(this.orderTable);
        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("사용자는 테이블 리스트를 조회 할 수 있다.")
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

    @DisplayName("사용자는 테이블을 빈 테이블(empty)로 셋팅 할 수 있다.")
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

    @DisplayName("사용자는 게스트의 숫자를 변경 할 수 있다.")
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

    @DisplayName("테이블을 조회하여 데이터가 있는지 체크한다.")
    @Test
    void changeEmptyFailedByTableGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 조회하여 데이터가 있는지 체크한다.")
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

    @DisplayName("테이블의 주문 상태가 Cooking, Meal 상태가 아닌지 체크한다.")
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

    @DisplayName("게스트의 숫가자 음수인지 체크한다.")
    @Test
    void changeNumberOfGuestFailedByNumberOfGuests() {
        // given
        orderTable.setNumberOfGuests(-2);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블id를 통하여 데이터가 있는지 체크한다.")
    @Test
    void changeNumberOfGuestFailedByOrderTable() {
        // given
        orderTable.setNumberOfGuests(2);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블id를 통하여 데이터가 있는지 체크한다.")
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