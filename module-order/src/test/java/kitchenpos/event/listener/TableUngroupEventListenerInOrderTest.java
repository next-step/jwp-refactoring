package kitchenpos.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.event.TableUngroupedEvent;
import kitchenpos.event.TableUngroupEvent;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableUngroupEventListenerInOrderTest {

    private TableUngroupEventListenerInOrder eventListener;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void init() {
        eventListener = new TableUngroupEventListenerInOrder(orderRepository);
    }

    @Test
    @DisplayName("완료가 안된 오더가 있으면 테이블 그룹 해제가 안됩니다")
    public void unGroupWithUnCompleteOrderThrowError() {
        //given
        TableUngroupedEvent eventDTO = new TableUngroupedEvent(Arrays.asList(1L));
        TableUngroupEvent event = new TableUngroupEvent(eventDTO);

        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            eventDTO.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .thenReturn(true);

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("테이블에 완료가 안된 주문이 있습니다");

    }
}