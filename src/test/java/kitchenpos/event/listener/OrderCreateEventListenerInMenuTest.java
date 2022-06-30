package kitchenpos.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.dto.event.OrderCreatedEvent;
import kitchenpos.event.customEvent.OrderCreateEvent;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderCreateEventListenerInMenuTest {

    private OrderCreateEventListenerInMenu eventListener;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    public void init() {
        eventListener = new OrderCreateEventListenerInMenu(menuRepository);
    }

    @Test
    @DisplayName("주문 넣은 메뉴가 존재하지 않으면 에러를 발생한다")
    public void menuMappedByOrderLineItemsIsExistTest() {
        //given
        OrderCreatedEvent eventDTO = new OrderCreatedEvent(1L, Arrays.asList(1L, 2L));
        OrderCreateEvent event = new OrderCreateEvent(eventDTO);

        //when
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(OrderException.class)
            .hasMessage("메뉴가 존재하지 않습니다");
    }

}