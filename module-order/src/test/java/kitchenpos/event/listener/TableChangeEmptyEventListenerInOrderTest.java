package kitchenpos.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.event.TableEmptyChangedEvent;
import kitchenpos.event.TableChangeEmptyEvent;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableChangeEmptyEventListenerInOrderTest {

    private TableChangeEmptyEventListenerInOrder eventListener;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void init() {
        eventListener = new TableChangeEmptyEventListenerInOrder(orderRepository);
    }

    @Test
    @DisplayName("테이블에 존재하는 오더중 완료되지 않은 오더가 있으면 에러가 발생한다")
    public void changeEmptyWithNotCompleteOrderThrowErrorTest() {
        //given
        Long orderTableId = 1L;
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .thenReturn(true);
        TableChangeEmptyEvent event = new TableChangeEmptyEvent(
            new TableEmptyChangedEvent(orderTableId));

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(OrderTableException.class)
            .hasMessage("완료되지않은 주문이 있으면 상태변경을 할수 없습니다");
    }


}