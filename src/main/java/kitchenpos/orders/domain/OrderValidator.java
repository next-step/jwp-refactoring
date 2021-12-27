package kitchenpos.orders.domain;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@Component
public class OrderValidator {

	private final OrderTableRepository orderTableRepository;
	private final MenuRepository menuRepository;

	public OrderValidator(OrderTableRepository orderTableRepository, MenuRepository menuRepository) {
		this.orderTableRepository = orderTableRepository;
		this.menuRepository = menuRepository;
	}

	public void validateOrderCreate(Order order) {
		validateOrderLineItemIsEmpty(order);
		validateIsMenuExisted(order);
		validateOrderTableIsUnUse(findOrderTableById(order.getOrderTableId()));
	}

	private void validateOrderLineItemIsEmpty(Order order) {
		if (order.isEmptyOrderLineItems()) {
			throw new IllegalArgumentException("주문할 메뉴를 골라주세요");
		}
	}

	private void validateIsMenuExisted(Order order) {
		order.getOrderLineItems()
			.forEach(ol -> findMenuById(ol.getMenuId()));
	}

	private Menu findMenuById(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다"));
	}

	private void validateOrderTableIsUnUse(OrderTable orderTable) {
		if (orderTable.isNotUse()) {
			throw new IllegalArgumentException("테이블이 비어있습니다");
		}
	}

	private OrderTable findOrderTableById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다"));
	}
}
