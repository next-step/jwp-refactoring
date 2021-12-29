package kitchenpos.tablegroup.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;

@Component
public class TableGroupValidator {
	public static final int TABLE_GROUP_MIN_SIZE = 2;
	private final OrderRepository orderRepository;

	public TableGroupValidator(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public void validateUngroup(List<OrderTable> orderTables) {
		List<Order> orders = orderRepository.findByOrderTableIdIn(extractedIds(orderTables));
		validateOrderIsCompletion(orders);
	}

	private List<Long> extractedIds(List<OrderTable> orderTables) {
		return orderTables.stream()
			.map(orderTable -> orderTable.getId())
			.collect(Collectors.toList());
	}

	private void validateOrderIsCompletion(List<Order> orders) {
		if (!isOrderCompletion(orders)) {
			throw new IllegalArgumentException("아직 테이블의 주문이 계산완료되지 않았습니다");
		}
	}

	private boolean isOrderCompletion(List<Order> orders) {
		return orders.stream()
			.allMatch(order -> order.isCompletion());
	}

	public void validateCreate(List<OrderTable> orderTables) {
		validateOrderTableSizeLessThanTwo(orderTables);
		validateOrderTableIsUseOrIsGrouped(orderTables);
	}

	private void validateOrderTableSizeLessThanTwo(List<OrderTable> orderTables) {
		if (IsSizeLessThanTwo(orderTables)) {
			throw new IllegalArgumentException("2개 이상의 테이블만 그룹생성이 가능합니다");
		}
	}

	private boolean IsSizeLessThanTwo(List<OrderTable> orderTables) {
		return CollectionUtils.isEmpty(orderTables) || orderTables.size() < TABLE_GROUP_MIN_SIZE;
	}

	private void validateOrderTableIsUseOrIsGrouped(List<OrderTable> orderTables) {
		if (isUseOrIsGrouped(orderTables)) {
			throw new IllegalArgumentException("이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
		}
	}

	private boolean isUseOrIsGrouped(List<OrderTable> orderTables) {
		return orderTables.stream()
			.anyMatch(orderTable -> orderTable.isUseOrIsGrouped());
	}
}


