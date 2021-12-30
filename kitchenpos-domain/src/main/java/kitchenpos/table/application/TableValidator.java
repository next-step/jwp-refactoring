package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@Component
public class TableValidator {

	private final OrderRepository orderRepository;

	public TableValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public void validateUnGroup(List<OrderTable> tables) {
		List<Long> orderTableIds = tables.stream().map(OrderTable::getId).collect(
			Collectors.toList());
		if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new AppException(ErrorCode.WRONG_INPUT, "조리 중이거나 식사 중인 테이블이 있으면 단체 해제가 안됩니다");
		}
	}
}
