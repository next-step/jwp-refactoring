package kitchenpos.ordertable.domain;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.exception.CanNotEditOrderTableEmptyByStatusException;

@Component
public class OrderTableExternalValidator {

	private final List<OrderStatus> COOKING_OR_MEAL = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

	private final OrderRepository orderRepository;

	public OrderTableExternalValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public void changeEmpty(Long orderTableId) {
		if (orderRepository.existsByOrderTable_IdAndOrderStatusIn(orderTableId, COOKING_OR_MEAL)) {
			throw new CanNotEditOrderTableEmptyByStatusException();
		}
	}
}
