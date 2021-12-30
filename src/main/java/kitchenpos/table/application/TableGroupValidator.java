package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
@Transactional(readOnly = true)
public class TableGroupValidator {

	private static final int MINIMUM = 2;

	private final OrderTableRepository orderTableRepository;

	public TableGroupValidator(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
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

}
