package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, 1, false);
        orderTable2 = new OrderTable(2L, 1, false);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.save(any())).willReturn(orderTable1);

        // when
        OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);

        // then
        assertThat(createdOrderTable.getId()).isEqualTo(orderTable1.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(orderTable1.isEmpty());
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmptyTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable1));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        OrderTableResponse changedOrderTable = tableService.changeEmpty(orderTable1.getId(), orderTableRequest);

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 이미 등록된 주문 테이블 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 주문의 상태가 완료 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable3() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable1));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable1));

        // when
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(orderTable1.getId(), orderTableRequest);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님수를 변경시 등록된 주문 테이블 이어야 한다.")
    @Test
    void changeNumberOfGuestsTest_unregisteredOrderTable() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(1, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
    }
}
