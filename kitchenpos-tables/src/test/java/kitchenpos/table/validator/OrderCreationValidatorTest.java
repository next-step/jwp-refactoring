package kitchenpos.table.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderCreationValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 검증기")
@ExtendWith(MockitoExtension.class)
class OrderCreationValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderCreationValidator orderCreationValidator;

    @DisplayName("주문 테이블이 빈 테이블이면 주문을 생성할 수 없다.")
    @Test
    void 주문_테이블이_빈_테이블이면_예외처리() {
        // given
        Order 새_주문 = Order.of(1L, OrderLineItems.create());

        OrderTable 빈_테이블 = OrderTable.of(3, true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(빈_테이블));

        // when / then
        assertThatThrownBy(() -> orderCreationValidator.validate(새_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void 존재하지_않는_주문_테이블_예외처리() {
        // given
        Order 새_주문 = Order.of(1L, OrderLineItems.create());
        given(orderTableRepository.findById(eq(1L))).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> orderCreationValidator.validate(새_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목이 비어있으면 주문을 생성할 수 없다.")
    @Test
    void 주문_항목이_없는_주문_예외처리() {
        Order 주문_항목이_비어있는_주문 = new Order.Builder().orderTableId(1L).orderStatus(OrderStatus.COOKING)
                .orderLineItems(OrderLineItems.create()).orderedTime(LocalDateTime.now()).build();
        OrderTable 주문_테이블 = OrderTable.of(4, false);

        given(orderTableRepository.findById(eq(1L))).willReturn(Optional.of(주문_테이블));

        assertThatThrownBy(() -> orderCreationValidator.validate(주문_항목이_비어있는_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
