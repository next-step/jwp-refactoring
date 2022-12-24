package kitchenpos.application.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(4, true);
        when(orderTableRepository.save(any())).thenReturn(orderTable);

        // when
        OrderTableResponse result = tableService.create(new OrderTableRequest(1L, 6, true));

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(orderTable.getId()),
            () -> assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블을 조회할 수 있다.")
    @Test
    void findAllOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable(4, true);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable1));
        // when
        List<OrderTableResponse> results = tableService.list();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(orderTable1.getId()),
            () -> assertThat(results.get(0).getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests())
        );
    }

    @DisplayName("등록되지 않은 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateNotExistOrderTableEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(4, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableRequest(1L, 6, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 조리 or 식사 상태라면 비어있는 상태 변경시 예외가 발생한다.")
    @Test
    void updateWrongOrderStatusEmptyException() {
        // given
        OrderTable orderTable1 = new OrderTable(6, true);
        OrderTableRequest request = new OrderTableRequest(1L, 6, true);
        when(orderTableRepository.findById(orderTable1.getId())).thenReturn(Optional.ofNullable(orderTable1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable1.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        )).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable1.getId(), request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable(4, true);
        orderTable.changeEmptyStatus(false);
        orderTable.changeNumberOfGuests(20);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

        // when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTable.getId(),  new OrderTableRequest(1L, 6, true));

        // then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(6);
    }


    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void notExistOrderTableUpdateException() {
        // when && then
        OrderTable orderTable = new OrderTable(4, true);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(1L, 6, true)))
            .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("빈 주문 테이블에 대해 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void emptyOrderTableUpdateException() {
        // given
        OrderTable orderTable = new OrderTable(6, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.ofNullable(orderTable));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(1L, 6, true)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

