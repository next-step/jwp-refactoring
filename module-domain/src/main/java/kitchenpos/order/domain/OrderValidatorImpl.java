package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.dto.OrderLineItemDto;

@Component
public class OrderValidatorImpl implements OrderValidator {
	private final OrderTables orderTables;
	private final Menus menus;

	public OrderValidatorImpl(OrderTables orderTables, Menus menus) {
		this.orderTables = orderTables;
		this.menus = menus;
	}

	@Override
	public void validateOrderTableExistAndNotEmpty(Long orderTableId) {
		if (!orderTables.contains(orderTableId)) {
			throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
		}

		OrderTable orderTable = orderTables.findById(orderTableId);
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException("주문 테이블이 비어있으면 주문을 생성할 수 없습니다.");
		}
	}

	@Override
	public void validateMenusExist(List<OrderLineItemDto> orderLineItemDtos) {
		List<Long> menuIds = orderLineItemDtos
			.stream()
			.map(OrderLineItemDto::getMenuId)
			.collect(Collectors.toList());

		if (!menus.containsAll(menuIds)) {
			throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
		}
	}
}
