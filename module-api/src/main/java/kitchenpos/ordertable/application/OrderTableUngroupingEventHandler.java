package kitchenpos.ordertable.application;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertablegroup.domain.OrderTableUngroupingEvent;

@Component
public class OrderTableUngroupingEventHandler {
	private final OrderTableRepository orderTableRepository;

	public OrderTableUngroupingEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@EventListener
	@Transactional
	public void handle(OrderTableUngroupingEvent event) {
		List<OrderTable> orderTables = orderTableRepository.findByOrderTableGroupId(event.getOrderTableGroupId());
		orderTables.forEach(OrderTable::ungrouped);
	}
}
