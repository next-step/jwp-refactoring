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
import kitchenpos.table.domain.OrderTableRepository;

@Component
@Transactional(readOnly = true)
public class TableGroupValidator {

	private static final int MINIMUM = 2;

	private final OrderTableRepository orderTableRepository;
	private final OrderRepository orderRepository;

	public TableGroupValidator(OrderTableRepository orderTableRepository,
		OrderRepository orderRepository) {
		this.orderTableRepository = orderTableRepository;
		this.orderRepository = orderRepository;
	}

	public void validateCreate(List<Long> orderTableIds) {
		List<OrderTable> orderTables = getOrderTables(orderTableIds);
		if (orderTables.size() != orderTableIds.size()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "주문 테이블들이 모두 등록되어있어야 합니다");
		}
		if (orderTables.size() < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "단체 지정은 주문 테이블이 {} 개 이상이어야 합니다", MINIMUM);
		}
		if (hasAnyEmptyTable(orderTables)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "단체 지정은 빈 테이블이 있으면 안됩니다");
		}
	}

	private List<OrderTable> getOrderTables(List<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	private boolean hasAnyEmptyTable(List<OrderTable> orderTables) {
		return orderTables.stream().anyMatch(OrderTable::isEmpty);
	}

	public void validateUnGroup(List<OrderTable> tables) {
		List<Long> orderTableIds = tables.stream().map(OrderTable::getId).collect(
			Collectors.toList());
		if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
			throw new AppException(ErrorCode.WRONG_INPUT, "조리 중이거나 식사 중인 테이블이 있으면 단체 해제가 안됩니다");
		}
	}

}
