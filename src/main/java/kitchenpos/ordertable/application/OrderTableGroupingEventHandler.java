package kitchenpos.ordertable.application;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertablegroup.domain.OrderTableGroupingEvent;

@Component
public class OrderTableGroupingEventHandler {
	private final OrderTableRepository orderTableRepository;

	public OrderTableGroupingEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@EventListener
	@Transactional
	public void handle(OrderTableGroupingEvent event) {
		List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(event.getOrderTableIds());
		orderTables.forEach(orderTable -> orderTable.groupedBy(event.getOrderTableGroupId()));
	}
}
