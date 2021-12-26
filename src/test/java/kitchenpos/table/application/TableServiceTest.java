package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 생성")
    @Test
    void 주문테이블_생성() {
        // given
        OrderTable orderTable = OrderTable.of(3);
        OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);

        given(orderTableRepository.save(orderTable)).willReturn(savedOrderTable);

        // when
        OrderTableResponse result = tableService.create(orderTable);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNumberOfGuests()).isEqualTo(3);
        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void 주문테이블_목록_조회() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);

        given(orderTableRepository.findAll()).willReturn(
            Lists.newArrayList(orderTable1, orderTable2));

        // when
        List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNumberOfGuests()).isEqualTo(3);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("비어있는 테이블로 변경")
    @Test
    void 빈_테이블_변경() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        OrderTable orderTableForUpdate = new OrderTable(1L, null, 3, true);

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L,
            Lists.newArrayList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        given(orderTableRepository.save(any())).willReturn(orderTableForUpdate);

        // when
        OrderTableResponse result = tableService.changeEmpty(1L, orderTableForUpdate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNumberOfGuests()).isEqualTo(3);
        assertThat(result.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹이 있는데 빈 테이블로 변경하면 예외 발생")
    @Test
    void 빈_테이블_변경_예외1() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(1L, orderTable)
        );
    }

    @DisplayName("주문 상태가 요리중이거나 식사 상태일 때, 빈 테이블로 변경하면 예외 발생")
    @Test
    void 빈_테이블_변경_예외2() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L,
            Lists.newArrayList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(1L, orderTable)
        );
    }

    @DisplayName("테이블 인원 변경")
    @Test
    void 테이블_인원_변경() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        OrderTable orderTableForUpdate = new OrderTable(1L, null, 4, false);

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(orderTable)).willReturn(orderTableForUpdate);

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(1L, orderTableForUpdate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("빈 테이블의 인원을 변경하는 경우 예외 발생")
    @Test
    void 테이블_인원_변경_예외() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        OrderTable orderTableForUpdate = new OrderTable(1L, null, 3, true);

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeNumberOfGuests(1L, orderTableForUpdate)
        );
    }

}