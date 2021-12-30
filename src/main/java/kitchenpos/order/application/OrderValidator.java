package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
@Transactional(readOnly = true)
public class OrderValidator {

	private final OrderTableRepository orderTableRepository;
	private final MenuRepository menuRepository;

	public OrderValidator(OrderTableRepository orderTableRepository,
		MenuRepository menuRepository) {
		this.orderTableRepository = orderTableRepository;
		this.menuRepository = menuRepository;
	}

	public void validateCreate(Long orderTableId) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "주문 테이블을 찾을 수 없습니다"));
		if (orderTable.isEmpty()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 주문 테이블입니다.");
		}
	}

	public void validateCreate(OrderRequest orderRequest) {
		OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "주문 테이블을 찾을 수 없습니다"));
		if (orderTable.isEmpty()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 주문 테이블입니다.");
		}
		validateOrderItems(orderRequest.getOrderLineItems());
	}

	private void validateOrderItems(List<OrderLineItemRequest> orderLineItems) {
		orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.forEach(this::validateMenuId);
	}

	private void validateMenuId(Long menuId) {
		if (!menuRepository.existsById(menuId)) {
			throw new AppException(ErrorCode.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다");
		}
	}

}
