package kitchenpos.table.event;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderTableEventTest {
    private OrderTableChangedEmptyHandler changedEmptyHandler;
    private OrderTableChangedEmptyEvent changedEmptyEvent;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        changedEmptyHandler = new OrderTableChangedEmptyHandler(orderRepository);
    }

    @DisplayName("테이블 비우기")
    @Test
    void empty() {
        OrderTable orderTable = new OrderTable(2, false);
        Order order = new Order(1L, orderTable.getId(), OrderStatus.COMPLETION);

        given(orderRepository.findAllByOrderTableId(any())).willReturn(Arrays.asList(order));

        주문_테이블_비우기(orderTable);
    }

    @DisplayName("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.")
    @Test
    void emptyOrderTable_예외2() {
        OrderTable orderTable = new OrderTable(2, false);

        changedEmptyEvent = new OrderTableChangedEmptyEvent(orderTable);

        주문_상태가_조리_혹은_식사일때_테이블_비울경우_예외_발생함();
    }

    private void 주문_테이블_비우기(OrderTable orderTable) {
        changedEmptyEvent = new OrderTableChangedEmptyEvent(orderTable);
        changedEmptyHandler.changedEmpty(changedEmptyEvent);
    }

    private void 주문_상태가_조리_혹은_식사일때_테이블_비울경우_예외_발생함() {
        assertThatThrownBy(() -> changedEmptyHandler.changedEmpty(changedEmptyEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다");
    }
}
