package kitchenpos.order.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

	private final OrderTableRepository orderTableRepository;

	public OrderValidator(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	public void validateCreate(Long orderTableId) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "주문 테이블을 찾을 수 없습니다"));
		if (orderTable.isEmpty()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 주문 테이블입니다.");
		}
	}

}
