package kitchenpos.table.application;

import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.table.dto.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void create() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //then
        assertThat(
                tableService.create(new OrderTableRequest(null, orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                        .getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    void list() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 2, true);
        OrderTable orderTable2 = new OrderTable(2L, TableGroup.empty(), 10, false);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(tableService.list().stream().map(OrderTableResponse::getId)).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다.")
    void changeEmpty() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 2, false);
        given(orderTableRepository.findByIdAndTableGroupIsNull(any())).willReturn(Optional.of(orderTable));
        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        OrderTableResponse updatedOrderTable =
                tableService.changeEmpty(orderTable.getId(), new OrderTableUpdateEmptyRequest(true));

        //then
        assertThat(updatedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 조회되지 않으면 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_1() {
        //given
        given(orderTableRepository.findByIdAndTableGroupIsNull(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(0L, new OrderTableUpdateEmptyRequest(true))).isExactlyInstanceOf(
                NoSuchElementException.class);
    }

    @Test
    @DisplayName("저장된 주문 테이블의 단체 지정이 되어있으면 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_2() {
        //given
        given(orderTableRepository.findByIdAndTableGroupIsNull(any())).willReturn(
                Optional.of(new OrderTable(1L, null, 2, false)));
        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).willReturn(true);
        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(0L, new OrderTableUpdateEmptyRequest(true))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 상태가 조리, 식사 인 경우 빈 테이블로 변경 실패한다.")
    void changeEmpty_failed_3() {
        //given
        given(orderTableRepository.findByIdAndTableGroupIsNull(any())).willReturn(
                Optional.of(new OrderTable(1L, null, 2, false)));
        given(ordersRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).willReturn(true);

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(0L, new OrderTableUpdateEmptyRequest(true))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(
                Optional.of(new OrderTable(1L, TableGroup.empty(), 5, false)));

        //when
        OrderTableResponse updatedOrderTable =
                tableService.changeNumberOfGuests(0L, new OrderTableUpdateNumberOfGuestsRequest(0));

        //then
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님 수가 음수면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_1() {
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L,
                new OrderTableUpdateNumberOfGuestsRequest(-1))).isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 테이블이 조회가 안 되면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_2() {
        //given
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L,
                new OrderTableUpdateNumberOfGuestsRequest(10))).isExactlyInstanceOf(NoSuchElementException.class);

    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 방문한 손님 수 변경에 실패한다.")
    void changeNumberOfGuests_failed_3() {
        //given
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.of(orderTable));

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L,
                new OrderTableUpdateNumberOfGuestsRequest(10))).isExactlyInstanceOf(IllegalArgumentException.class);

    }
}
