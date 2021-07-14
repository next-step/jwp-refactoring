package kitchenpos.menu.application.event;

import kitchenpos.menu.application.exception.BadMenuIdException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.event.OrderCreatedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderEventHandlerTest {

    @InjectMocks
    private OrderEventHandler orderEventHandler;

    @Mock
    private MenuRepository menuRepository;

    @Test
    @DisplayName("주문은 중복된 메뉴를 설정할 수 없다.")
    void createOrderException() {
        //given
        Order order = Order.of(null, null, Lists.list(
                OrderLineItem.of(1L, 1L),
                OrderLineItem.of(1L, 1L),
                OrderLineItem.of(1L, 1L)
        ));
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order);
        given(menuRepository.countByIdIn(any())).willReturn(1);

        //when
        assertThatThrownBy(() -> orderEventHandler.createOrderEventListener(orderCreatedEvent))
                .isInstanceOf(BadMenuIdException.class); //then
    }
}
