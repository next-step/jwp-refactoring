package kitchenpos.table.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.domain.OrderCookingEvent;

@Component
public class OrderCookingEventHandler {

	private final OrderTableRepository orderTableRepository;

	public OrderCookingEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Async
	@EventListener
	@Transactional
	public void handle(OrderCookingEvent event) {
		OrderTable orderTable = getOrderTable(event.getOrderTableId());
		orderTable.changeUnorderable();
	}

	private OrderTable getOrderTable(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
								   .orElseThrow(EntityNotFoundException::new);
	}
}
