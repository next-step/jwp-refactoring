package kitchenpos.order.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderTableValidateEvent;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.OrderException;

@Component
public class OrderValidator {

	private final ApplicationEventPublisher eventPublisher;
	private final MenuRepository menuRepository;

	public OrderValidator(final ApplicationEventPublisher eventPublisher, final MenuRepository menuRepository) {
		this.eventPublisher = eventPublisher;
		this.menuRepository = menuRepository;
	}

	public void validate(OrderRequest orderRequest) {
		orderTableFindById(orderRequest.getOrderTableId());
		orderRequest.getOrderLineItems()
			.forEach(orderLineItemRequest -> existsMenuById(orderLineItemRequest.getMenuId()));
	}

	private void orderTableFindById(Long orderTableId) {
		eventPublisher.publishEvent(OrderTableValidateEvent.from(orderTableId));
	}

	private void existsMenuById(Long menuId) {
		if (!menuRepository.existsById(menuId)) {
			throw new OrderException(ErrorCode.MENU_IS_NULL);
		}
	}
}
