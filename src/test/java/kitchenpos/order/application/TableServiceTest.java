package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableState;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 빈테이블;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(1L, 1, new TableState(false));
        테이블2 = new OrderTable(2L, 3, new TableState(false));
        빈테이블 = new OrderTable(2L, 0, new TableState(true));
    }

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        OrderTableRequest request = new OrderTableRequest(2L, 0, true);

        when(orderTableRepository.save(any())).thenReturn(빈테이블);
        OrderTableResponse response = tableService.create(request);

        verify(orderTableRepository, times(1)).save(any(OrderTable.class));
        assertThat(response).extracting("id", "numberOfGuests", "tableState")
                .containsExactly(빈테이블.getId(), 빈테이블.getNumberOfGuests(), 빈테이블.isEmpty());
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void list() {
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(테이블1, 빈테이블));

        List<OrderTableResponse> responses = tableService.list();

        verify(orderTableRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("테이블을 빈 테이블로 변경한다.")
    void changeEmpty() {
        테이블1.changeStatus(COMPLETION);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(테이블1));

        OrderTableResponse response = tableService.changeEmpty(테이블1.getId());

        verify(orderTableRepository, times(1)).findById(anyLong());
        assertThat(response.getTableState()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우 예외가 발생한다.")
    void validateOrderTable() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(테이블1.getId()))
                .isInstanceOf(TableNotFoundException.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("테이블 그룹으로 등록되어 있는 경우 예외가 발생한다.")
    void validateExistTableGroup() {
        테이블1.setTableGroup(new TableGroup(LocalDateTime.now(), Arrays.asList(테이블1, 테이블2)));
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(테이블1));

        assertThatThrownBy(() -> tableService.changeEmpty(테이블1.getId()))
                .isInstanceOf(InvalidTableState.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("테이블의 주문 상태가 완료가 아닌 경우 예외가 발생한다.")
    void validateOrderStatus() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));

        assertThatThrownBy(() -> tableService.changeEmpty(테이블1.getId()))
                .isInstanceOf(InvalidOrderState.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        OrderTableRequest request = OrderTableRequest.of(5);

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));
        OrderTableResponse response = tableService.changeGuests(테이블1.getId(), request);

        verify(orderTableRepository, times(1)).findById(anyLong());
        assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블이 없는 경우 예외가 발생한다.")
    void validateOrderTable2() {
        OrderTableRequest request = OrderTableRequest.of(5);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeGuests(테이블1.getId(), request))
                .isInstanceOf(TableNotFoundException.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        OrderTableRequest request = OrderTableRequest.of(2);
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeGuests(빈테이블.getId(), request))
                .isInstanceOf(TableNotFoundException.class);
        verify(orderTableRepository, times(1)).findById(anyLong());
    }
}