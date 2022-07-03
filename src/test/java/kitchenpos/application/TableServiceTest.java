package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.OrderStatusException;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    TableService tableService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    OrderTable 주문테이블;

    @Test
    @DisplayName("주문테이블을 생성한다(Happy Path)")
    void create() {
        //given
        주문테이블 = new OrderTable(1L, 2, false);
        OrderTableRequest 주문테이블Request = new OrderTableRequest(2, false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블);

        //when
        OrderTableResponse savedOrderTableGroup = tableService.create(주문테이블Request);

        //then
        assertThat(savedOrderTableGroup).isNotNull()
                .satisfies(orderTable -> {
                            orderTable.getId().equals(주문테이블.getId());
                            Objects.isNull(orderTable.getTableGroupId());
                            String.valueOf(orderTable.getNumberOfGuests()).equals(String.valueOf(주문테이블.getNumberOfGuests()));
                        }
                );
    }

    @Test
    @DisplayName("주문테이블 리스트를 조회한다.")
    void list() {
        //given
        주문테이블 = new OrderTable(1L, 2, true);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문테이블));

        //when
        List<OrderTableResponse> orderTables = tableService.list();

        //then
        assertAll(() -> {
            assertThat(orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList()))
                            .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블.getId()));
            assertThat(orderTables.stream().map(OrderTableResponse::getNumberOfGuests).collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블.getNumberOfGuests()));
            assertThat(orderTables.stream().map(OrderTableResponse::isEmpty).collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블.isEmpty()));
        });
    }

    @Test
    @DisplayName("테이블 비어있는지 여부 변경 (Happy Path)")
    void changeEmpty() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, true);
        boolean 변경후상태 = false;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        //when
        OrderTableResponse changedOrderTable = tableService.changeEmpty(주문테이블.getId(), 변경후상태);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 테이블일 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidTableId() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, true);
        boolean 변경후상태 = false;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경후상태);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 테이블 그룹에 포함되어 있는 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidTableGroup() {
        //given
        주문테이블 = new OrderTable(1L, new TableGroup(2L, LocalDateTime.now()), 2, true);
        boolean 변경후상태 = false;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        //then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경후상태);
        }).isInstanceOf(OrderTableException.class)
        .hasMessageContaining(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
    }

    @Test
    @DisplayName("테이블의 주문상태가 조리중/식사중일 경우 테이블 비어있는지 여부 변경 불가")
    void changeEmptyInvalidOrderStatus() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, true);
        boolean 변경후상태 = false;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        //then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블.getId(), 변경후상태);
        }).isInstanceOf(OrderStatusException.class)
        .hasMessageContaining(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
    }

    @Test
    @DisplayName("테이블의 손님 숫자를 변경한다 (Happy Path)")
    void changeNumberOfGuests() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, false);
        int 변경후손님수 = 3;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //when
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(주문테이블.getId(), 변경후손님수);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(변경후손님수);
    }

    @Test
    @DisplayName("변경하려는 손님 숫자가 유효하지 않은 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidNumbers() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, false);
        int 변경후손님수 = -1;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경후손님수);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 테이블이 유효하지 않은 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidOrderTable() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, false);
        int 변경후손님수 = 3;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경후손님수);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 테이블이 비어있는 경우 테이블의 손님 숫자 변경 불가")
    void changeNumberOfGuestsInvalidOrderTableEmpty() {
        //given
        주문테이블 = new OrderTable(1L, null, 2, true);
        int 변경후손님수 = 3;
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블.getId(), 변경후손님수);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}