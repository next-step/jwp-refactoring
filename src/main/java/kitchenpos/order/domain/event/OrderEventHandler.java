package kitchenpos.order.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class OrderEventHandler {

	private final OrderTableRepository orderTableRepository;

	public OrderEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@EventListener
	@Async
	public void handle(OrderCreatedEvent event) {
		orderTableRepository.findById(event.tableId())
			.orElseThrow(() -> new NotFoundException(String.format("주문 테이블 id(%d)를 찾을 수 없습니다.", event.tableId())))
			.updateEmpty(false);
	}
}
