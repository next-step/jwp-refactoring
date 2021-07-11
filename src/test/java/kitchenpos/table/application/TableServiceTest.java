package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        final OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);
        final OrderTableResponse orderTableResponse = new OrderTableResponse(savedOrderTable);

        when(orderTableRepository.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);
        final OrderTableResponse actual = tableService.create(orderTableRequest);

        assertThat(actual).isEqualTo(orderTableResponse);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        final OrderTable savedOrderTable1 = new OrderTable(1L, null, 3, false);
        final OrderTable savedOrderTable2 = new OrderTable(2L, null, 3, false);
        final OrderTableResponse orderTableResponse1 = new OrderTableResponse(savedOrderTable1);
        final OrderTableResponse orderTableResponse2 = new OrderTableResponse(savedOrderTable2);

        when(orderTableRepository.findAll())
                .thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));

        List<OrderTableResponse> orderTables = tableService.list();

        assertThat(orderTables).containsAll(Arrays.asList(orderTableResponse1, orderTableResponse2));
    }

    @Test
    @DisplayName("주문 테이블을 비어있음 상태로 변경한다.")
    void change_order_empty() {
        final Long givenOrderTableId = 1L;
        final OrderTable savedOrderTable1 = new OrderTable(1L, null, 3, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        final OrderTableResponse actual = tableService.changeEmpty(givenOrderTableId, orderTableRequest);

        assertThat(actual.getEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블을 비울 시 테이블 그룹이 포함된 경우 예외를 던진다.")
    void change_order_empty_with_group_table() {
        final Long givenOrderTableId = 1L;
        final OrderTable orderTable = new OrderTable(1L, null, 4, false);
        final OrderTable orderTable2 = new OrderTable(1L, null, 4, false);
        TableGroup tableGroup = TableGroup.of(new OrderTables(Arrays.asList(orderTable, orderTable2)));
        final OrderTable savedOrderTable1 = new OrderTable(1L, 1L, 3, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTableId, orderTableRequest))
                .isInstanceOf(EntityExistsException.class);
    }

    @Test
    @DisplayName("방문 손님 수를 변경한다.")
    void change_number_of_guests() {
        Long givenOrderTableId = 1L;
        int updateNumberOfGuest = 5;
        final OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(updateNumberOfGuest, false);

        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        OrderTableResponse actual = tableService.changeNumberOfGuests(givenOrderTableId, orderTableRequest);

        assertThat(actual).extracting("numberOfGuests").isEqualTo(updateNumberOfGuest);
    }

    @Test
    @DisplayName("손님 수 변경시, 방문 손님 수가 없으면 예외를 던진다.")
    void change_with_empty_number_of_guests() {
        Long givenOrderTableId = 1L;
        int updateNumberOfGuest = -1;
//        TableGroup tableGroup = TableGroup.of(new OrderTables(new ArrayList<>()));
        final OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(updateNumberOfGuest, false);

        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 손님 수 변경시 예외를 던진다.")
    void change_number_of_guest_with_empty_order_table() {
        Long givenOrderTableId = 1L;
        int updateNumberOfGuest = 5;
        final OrderTable savedOrderTable = new OrderTable(1L, null, 3, true);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(updateNumberOfGuest, false);

        when(orderTableRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalStateException.class);
    }
}
