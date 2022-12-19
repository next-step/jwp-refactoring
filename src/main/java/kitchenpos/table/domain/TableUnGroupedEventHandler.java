package kitchenpos.table.domain;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.tablegroup.domain.TableUnGroupedEvent;

@Component
public class TableUnGroupedEventHandler {
	private final OrderTableRepository orderTableRepository;
	private final OrderValidator orderValidator;

	public TableUnGroupedEventHandler(OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
		this.orderTableRepository = orderTableRepository;
		this.orderValidator = orderValidator;
	}

	@EventListener
	public void handle(TableUnGroupedEvent event) {
		List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
		orderTables.forEach(it -> orderValidator.unGroupOrderStatusValidate(it.getId()));
		OrderTables.of(orderTables).unGroup();
	}

}
