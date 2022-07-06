package kitchenpos.application.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import java.util.Optional;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        OrderTable orderTable = OrderTable.of(4, true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        when(orderTableRepository.save(any())).thenReturn(orderTable);

        OrderTableResponse saved = tableService.create(orderTableRequest);

        assertThat(saved.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
        assertThat(saved.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void findAll() {
        OrderTable orderTable = OrderTable.of(4, true);
        when(orderTableRepository.findAll()).thenReturn(Lists.list(orderTable));

        List<OrderTableResponse> tables = tableService.list();

        assertThat(tables).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부를 수정하려고 하면 예외를 반환한다.")
    void changeEmptyWithNoExistingOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(99L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 이미 단체 지정되어있으면 예외를 반환한다.")
    void changeEmptyWithGroupedOrderTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        OrderTable orderTable =  OrderTable.of(4, true);
        orderTable.registerGroupTable(new TableGroup());
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            tableService.changeEmpty(1L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 빈 테이블 여부가 수정된다.")
    void changeEmpty() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        OrderTable orderTable =  OrderTable.of(4, true);
        OrderTable updateTable =  OrderTable.of(4, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableRepository.save(any())).thenReturn(updateTable);

        tableService.changeEmpty(1L, orderTableRequest);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("빈 테이블은 손님수를 수정할 수 없다.")
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(5, true);
        OrderTable orderTable =  OrderTable.of(4, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("0명 미만으로 손님수를 수정할 수 없다.")
    void changeNumberOfGuestsWithInvalidNumber() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);
        OrderTable orderTable =  OrderTable.of(4, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(1L, orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("손님수를 수정한다.")
    void changeNumber() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(5, true);
        OrderTable orderTable =  OrderTable.of(4, false);
        OrderTable updateTable = OrderTable.of(5, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableRepository.save(any())).thenReturn(updateTable);

        tableService.changeNumberOfGuests(1L, orderTableRequest);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }
}
