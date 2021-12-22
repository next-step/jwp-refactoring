package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.exception.TableGroupException;

@Component
public class TableGroupValidator {

	private final OrderTableRepository orderTableRepository;
	private final OrderRepository orderRepository;

	public TableGroupValidator(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
		this.orderTableRepository = orderTableRepository;
		this.orderRepository = orderRepository;
	}

	public OrderTables findValidatedOrderTables(List<Long> ids) {
		OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByIdIn(ids));
		validateOrderTables(orderTables);
		return orderTables;
	}

	public OrderTables findCompletionOrderTables(Long tableId) {
		OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByTableGroupId(tableId));
		validateCompletionOrderTables(orderTables.getOrderTablesIds());
		return orderTables;
	}

	private void validateOrderTables(OrderTables orderTables) {
		if (orderTables.isEmpty()) {
			throw new TableGroupException(ErrorCode.ORDER_TABLE_IS_NULL);
		}

		if (orderTables.isOneTable()) {
			throw new TableGroupException(ErrorCode.NEED_MORE_ORDER_TABLES);
		}

		if (orderTables.findAnyNotEmptyTable()) {
			throw new TableGroupException(ErrorCode.ORDER_TABLE_IS_EMPTY);
		}
	}

	private void validateCompletionOrderTables(List<Long> ids) {
		if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(ids, OrderStatus.notCompletionStatus())) {
			throw new TableGroupException(ErrorCode.ORDER_IS_NOT_COMPLETION);
		}
	}
}
