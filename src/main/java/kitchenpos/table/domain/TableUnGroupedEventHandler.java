package kitchenpos.table.domain;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.tablegroup.domain.TableUnGroupedEvent;

@Component
public class TableUnGroupedEventHandler {
	private final OrderTableRepository orderTableRepository;
	private final OrderTableValidator orderTableValidator;

	public TableUnGroupedEventHandler(OrderTableRepository orderTableRepository,
		OrderTableValidator orderTableValidator) {
		this.orderTableRepository = orderTableRepository;
		this.orderTableValidator = orderTableValidator;
	}

	@EventListener
	@Transactional
	public void handle(TableUnGroupedEvent event) {
		List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
		orderTables.forEach(it -> orderTableValidator.unGroupOrderStatusValidate(it.getId()));
		OrderTables.of(orderTables).unGroup();
	}

}
