package kitchenpos.infra.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.exceptions.OrderTableEntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderTableAdapterInTableGroupTest {
    private OrderTableAdapterInTableGroup orderTableAdapterInTableGroup;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setup() {
        orderTableAdapterInTableGroup = new OrderTableAdapterInTableGroup(orderTableRepository);
    }

    @DisplayName("안전하게 주문 테이블 정보를 불러 올 수 있다.")
    @Test
    void findOrderTableTest() {
        // given
        Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(new OrderTable(0, true)));

        // when
        OrderTable orderTable = orderTableAdapterInTableGroup.getOrderTable(orderTableId);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 주문 테이블 요청 시 예외가 발생한다.")
    @Test
    void findOrderTableFailTest() {
        // given
        Long orderTableId = 1L;
        given(orderTableRepository.findById(orderTableId)).willThrow(new OrderTableEntityNotFoundException(""));

        // when, then
        assertThatThrownBy(() -> orderTableAdapterInTableGroup.getOrderTable(orderTableId))
                .isInstanceOf(OrderTableEntityNotFoundException.class);
    }
}