package kitchenpos.table.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.exception.TableGroupException;

@Component
public class OrderTableEventHandler {

	private final TableRepository tableRepository;
	private final OrderRepository orderRepository;

	public OrderTableEventHandler(TableRepository tableRepository, OrderRepository orderRepository) {
		this.tableRepository = tableRepository;
		this.orderRepository = orderRepository;
	}

	@EventListener
	public void groupOrderTable(OrderTableGroupEvent orderTableGroupEvent) {
		OrderTables orderTables = new OrderTables(
			tableRepository.findAllByIdIn(orderTableGroupEvent.getOrderTableIds()));
		orderTables.group(orderTableGroupEvent.getTableGroup());
	}

	@EventListener
	public void unGroupOrderTable(OrderTableUngroupEvent orderTableUngroupEvent) {
		TableGroup tableGroup = orderTableUngroupEvent.getTableGroup();
		List<OrderTable> orderTables = tableRepository.findAllByTableGroupId(tableGroup.getId());
		validateUngroup(orderTables);
		ungroup(orderTables);
	}

	private void validateUngroup(List<OrderTable> orderTables) {
		if (!isOrderCompletion(orderTables)) {
			throw new TableGroupException("조리중이거나 식사중일 경우 단체지정을 해체할 수 없다.");
		}
	}

	private boolean isOrderCompletion(List<OrderTable> orderTables) {
		return orderTables.stream()
			.anyMatch(orderTable -> {
				Order order = orderRepository.findByOrderTableId(orderTable.getId());
				return order.getOrderStatus() == OrderStatus.COMPLETION;
			});
	}

	public void ungroup(List<OrderTable> orderTables) {
		orderTables.forEach(orderTable -> {
			orderTable.unGroup();
		});
	}

}
