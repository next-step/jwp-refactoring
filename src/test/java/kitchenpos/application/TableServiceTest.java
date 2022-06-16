package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Test
    void create() {
        //given
        given(orderTableDao.save(any())).willReturn(new OrderTable());

        //then
        assertThat(tableService.create(new OrderTable())).isInstanceOf(OrderTable.class);
    }

    @Test
    void list() {
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(new OrderTable()));

        //then
        assertThat(tableService.list()).isNotEmpty();
    }

    @Test
    void changeEmpty() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(new OrderTable());

        //then
        assertThat(tableService.changeEmpty(0L, new OrderTable())).isExactlyInstanceOf(OrderTable.class);
    }

    @Test
    @DisplayName("orderTableId 가 조회되지 않으면 실패한다.")
    void changeEmpty_failed_1() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTable())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 주문 테이블의 테이블 그룹 id 가 비어있지 않으면 실패한다.")
    void changeEmpty_failed_2() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTable())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 COOKING, MEAL 인 경우 실패한다.")
    void changeEmpty_failed_3() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTable())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(new OrderTable());

        //then
        assertThat(tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(OrderTable.class);

    }

    @Test
    @DisplayName("손님 수가 음수면 실패한다.")
    void changeNumberOfGuests_failed_1() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }

    @Test
    @DisplayName("orderTableId 로 조회가 안 되면 실패한다.")
    void changeNumberOfGuests_failed_2() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable)).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }

    @Test
    @DisplayName("orderTable 이 비어있으면 실패한다.")
    void changeNumberOfGuests_failed_3() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, new OrderTable())).isExactlyInstanceOf(
                IllegalArgumentException.class);

    }
}
