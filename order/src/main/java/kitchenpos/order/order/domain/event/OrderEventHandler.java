package kitchenpos.order.order.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kitchenpos.order.table.application.OrderTableService;

@Component
public class OrderEventHandler {

	private final OrderTableService orderTableService;

	public OrderEventHandler(OrderTableService orderTableService) {
		this.orderTableService = orderTableService;
	}

	@EventListener
	@Async
	public void handle(OrderCreatedEvent event) {
		orderTableService.ordered(event.tableId());
	}
}
