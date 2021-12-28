package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.TableException;

@Component
public class TableValidator {

	private final OrderRepository orderRepository;

	public TableValidator(final OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void validate(OrderTable orderTable) {
		validateOrders(ordersFindByTableId(orderTable.getId()))
			.hasNotCompletionOrder();
	}

	private Orders validateOrders(List<Order> orderList) {
		if (orderList.isEmpty()) {
			throw new TableException(ErrorCode.ORDER_IS_NULL);
		}
		return Orders.from(orderList);
	}

	private List<Order> ordersFindByTableId(Long tableId) {
		return orderRepository.findAllByOrderTableId(tableId);
	}
}
