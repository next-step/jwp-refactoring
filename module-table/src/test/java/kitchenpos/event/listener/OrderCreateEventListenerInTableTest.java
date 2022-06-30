package kitchenpos.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.event.OrderCreatedEvent;
import kitchenpos.event.OrderCreateEvent;
import kitchenpos.exception.OrderException;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderCreateEventListenerInTableTest {

    private OrderCreateEventListenerInTable eventListener;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    public void init() {
        eventListener = new OrderCreateEventListenerInTable(orderTableRepository);
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 에러가 발생한다")
    public void OrderCreateWithNotSavedTableThrowError() {
        //given
        OrderCreatedEvent eventDTO = new OrderCreatedEvent(1L, Arrays.asList(1L, 2L));
        OrderCreateEvent event = new OrderCreateEvent(eventDTO);

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(OrderException.class)
            .hasMessage("주문이 실행될 테이블은 저장이 되어있어야합니다");
    }

    @Test
    @DisplayName("테이블이 비어있으면 에러가 발생한다")
    public void OrderCreateWithEmptyTableThrowError() {
        //given
        OrderCreatedEvent eventDTO = new OrderCreatedEvent(1L, Arrays.asList(1L, 2L));
        OrderCreateEvent event = new OrderCreateEvent(eventDTO);

        OrderTable orderTable_not_empty = new OrderTable(1, true);

        when(orderTableRepository.findById(1L))
            .thenReturn(Optional.of(orderTable_not_empty));

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(OrderException.class)
            .hasMessage("주문이 실행될 테이블은 비어있으면 안됩니다");
    }
}