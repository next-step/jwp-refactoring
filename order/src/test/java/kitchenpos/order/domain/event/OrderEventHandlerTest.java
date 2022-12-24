package kitchenpos.order.domain.event;

import static kitchenpos.generator.OrderTableGenerator.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@DisplayName("주문 이벤트 테스트")
@ExtendWith(MockitoExtension.class)
class OrderEventHandlerTest {

	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private OrderEventHandler orderEventHandler;

	@DisplayName("주문 생성 이벤트 테스트")
	@Test
	void handleEventTest() {
		//given
		OrderCreatedEvent event = mock(OrderCreatedEvent.class);
		OrderTable 비어있는_다섯명_테이블 = 비어있는_다섯명_테이블();
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(비어있는_다섯명_테이블));

		//when
		orderEventHandler.handle(event);

		//then
		assertThat(비어있는_다섯명_테이블.isEmpty()).isFalse();
	}

}
