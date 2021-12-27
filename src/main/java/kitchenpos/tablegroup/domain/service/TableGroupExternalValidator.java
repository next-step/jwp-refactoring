package kitchenpos.tablegroup.domain.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.repo.OrderRepository;
import kitchenpos.order.domain.domain.OrderStatus;
import kitchenpos.tablegroup.exception.CanNotUngroupByOrderStatusException;

@Component
public class TableGroupExternalValidator {

	private static final List<OrderStatus> COOKING_OR_MEAL = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

	private final OrderRepository orderRepository;

	public TableGroupExternalValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public void ungroup(List<Long> orderTableIds) {
		if (orderRepository.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds, COOKING_OR_MEAL)) {
			throw new CanNotUngroupByOrderStatusException();
		}
	}
}
