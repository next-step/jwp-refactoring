package kitchenpos.table.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.order.domain.OrderCompletedEvent;

@Component
public class OrderCompletedEventHandler {

	private final OrderTableRepository orderTableRepository;

	public OrderCompletedEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@Async
	@EventListener
	@Transactional
	public void handle(OrderCompletedEvent event) {
		OrderTable orderTable = getOrderTable(event.getOrderTableId());
		orderTable.changeOrderable();
		orderTable.detachTableGroup();
	}

	private OrderTable getOrderTable(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(EntityNotFoundException::new);
	}
}
