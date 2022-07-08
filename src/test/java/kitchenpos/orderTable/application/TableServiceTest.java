package kitchenpos.orderTable.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.dto.OrderTableRequest;
import kitchenpos.orderTable.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.orderTable.domain.OrderTableTest.주문_태이블_생성;
import static kitchenpos.tableGroup.domain.TableGroupTest.단체_지정_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, true);

        when(orderTableRepository.save(any()))
                .thenReturn(주문_태이블_생성(1L, null, request.getNumberOfGuests(), request.isEmpty()));

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getOrderTableId()).isNotNull(),
                () -> assertThat(response.getTableGroupId()).isNull(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(response.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void listOrderTable() {
        // given
        OrderTable orderTable1 = 주문_태이블_생성(1L, null, 0, true);
        OrderTable orderTable2 = 주문_태이블_생성(2L, null, 0, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableRepository.findAll()).thenReturn(orderTables);

        // when
        List<OrderTableResponse> list = tableService.list();

        // then
        assertThat(list).hasSize(orderTables.size());
    }

    @DisplayName("빈 주문 테이블로 상태변한다.")
    @Test
    void changeEmpty() {
        // given
        Long savedOrderId = 1L;
        OrderTable savedOrderTable = 주문_태이블_생성(savedOrderId, null, 0, false);
        when(orderTableRepository.findById(savedOrderId)).thenReturn(Optional.of(savedOrderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);

        // when, then
        tableService.changeEmpty(savedOrderId);
    }

    @DisplayName("단체 지정되어 있으면 안된다.")
    @Test
    void changeEmpty1() {
        // given
        Long savedOrderId = 1L;
        TableGroup tableGroup = 단체_지정_생성(1L, null);
        OrderTable savedOrderTable = 주문_태이블_생성(savedOrderId, tableGroup.getId(), 0, false);
        when(orderTableRepository.findById(savedOrderId)).thenReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당 테이블에 '조리' 또는 '식사' 상태의 주문이 존재하면 안된다.")
    @Test
    void changeEmpty2() {
        // given
        Long savedOrderId = 1L;
        OrderTable savedOrderTable = 주문_태이블_생성(savedOrderId, null, 0, false);
        when(orderTableRepository.findById(savedOrderId)).thenReturn(Optional.of(savedOrderTable));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        Long savedOrderTableId = 1L;
        int numberOfGuests = 1;
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, false);
        OrderTable savedOrderTable = 주문_태이블_생성(savedOrderTableId, null, 0, false);

        when(orderTableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));

        // when, then
        tableService.changeNumberOfGuests(savedOrderTableId, request);
    }

    @DisplayName("변경할 방문 손님의 수는 0 이상이여야 한다.")
    @Test
    void changeNumberOfGuests1() {
        // given
        Long savedOrderTableId = 1L;
        int numberOfGuests = -1;
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, false);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 비여있는 상태이면 안된다.")
    @Test
    void changeNumberOfGuests2() {
        // given
        Long savedOrderTableId = 1L;
        int numberOfGuests = 1;
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, true);
        OrderTable savedOrderTable = 주문_태이블_생성(savedOrderTableId, null, 0, true);

        when(orderTableRepository.findById(savedOrderTableId)).thenReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
