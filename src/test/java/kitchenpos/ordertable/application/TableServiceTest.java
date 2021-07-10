package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.InvalidOrderStatusException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.TableGroup;

@DisplayName("테이블 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;
    private OrderTableRequest OrderTableNumberOfGuestsRequest;

    @BeforeEach
    void setup() {
        orderTable = new OrderTable(1L, new NumberOfGuests(2), true);
        OrderTableNumberOfGuestsRequest = new OrderTableRequest(2, true);
    }

    @DisplayName("사용자는 테이블을 생성(예약) 할 수 있다.")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.save(any())).thenReturn(orderTable);
        OrderTableResponse orderTable = tableService.create(OrderTableNumberOfGuestsRequest);
        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("사용자는 테이블 리스트를 조회 할 수 있다.")
    @Test
    void list() {
        // given

        // when
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));
        List<OrderTableResponse> list = tableService.list();
        // then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(1);
    }

    @DisplayName("사용자는 테이블을 빈 테이블(empty)로 셋팅 할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(false);
        // when
        Order order = Order.of(1L, OrderStatus.COOKING);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        when(orderDao.findOrdersByOrderTableIdIn(any())).thenReturn(Arrays.asList(order));
        OrderTableResponse changedOrderTable = tableService.changeEmpty(1L, orderTableEmptyRequest);
        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("사용자는 게스트의 숫자를 변경 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest = new OrderTableNumberOfGuestsRequest(10);
        orderTable = new OrderTable(1L, new NumberOfGuests(2), false);
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(1L, orderTableNumberOfGuestsRequest);
        // then
        assertThat(changedOrderTable).isNotNull();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("테이블을 조회하여 데이터가 있는지 체크한다.")
    @Test
    void changeEmptyFailedByTableGroup() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyRequest(false)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("테이블을 조회하여 데이터가 있는지 체크한다.")
    @Test
    void changeEmptyFailedByTableGroupId() {
        // given
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyRequest(false)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("테이블의 주문 상태가 Cooking, Meal 상태가 아닌지 체크한다.")
    @Test
    void changeEmptyFailedByOrderStatus() {
        // given

        // when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyRequest(false)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("게스트의 숫가자 음수인지 체크한다.")
    @Test
    void changeNumberOfGuestFailedByNumberOfGuests() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableNumberOfGuestsRequest(-2)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("주문테이블id를 통하여 데이터가 있는지 체크한다.")
    @Test
    void changeNumberOfGuestFailedByOrderTable() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableNumberOfGuestsRequest(10)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("주문테이블id를 통하여 데이터가 있는지 체크한다.")
    @Test
    void changeNumberOfGuestFailedByOrderTableEmpty() {
        // given
        // when
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableNumberOfGuestsRequest(10)))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("그룹 세팅")
    @Test
    void setGroup() {
        // given
        // when
        when(orderTableDao.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(
                new OrderTable(1L, new NumberOfGuests(2), true),
                new OrderTable(2L, new NumberOfGuests(2), true)
        ));
        tableService.setGroup(new TableGroup(), Arrays.asList(1L, 2L));
        // then
        verify(orderTableDao, times(1)).saveAll(any());
    }

    @DisplayName("그룹 해제")
    @Test
    void ungroup(){
        // given
        // when
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(
                new OrderTable(1L, new NumberOfGuests(2), true),
                new OrderTable(2L, new NumberOfGuests(2), true)));

        when(orderDao.findOrdersByOrderTableIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(Order.of(1L, OrderStatus.COMPLETION)));

        tableService.ungroup(new TableGroup());
        // then
        verify(orderDao, times(1)).findOrdersByOrderTableIdIn(any());
    }

    @DisplayName("그룹 제거 시 주문 상태가 요리중, 식사중 상태가아닌지 체크한다.")
    @Test
    void ungroupFailedByCookingStatus() {
        // given
        Order order = Order.of(1L, OrderStatus.MEAL);
        // when
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(
                new OrderTable(1L, new NumberOfGuests(2), true),
                new OrderTable(2L, new NumberOfGuests(2), true)));
        when(orderDao.findOrdersByOrderTableIdIn(any())).thenReturn(Arrays.asList(order));

        // then
        assertThatThrownBy(() -> tableService.ungroup(new TableGroup()))
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}