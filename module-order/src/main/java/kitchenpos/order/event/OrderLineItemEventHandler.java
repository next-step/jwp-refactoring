package kitchenpos.order.event;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

@Component
public class OrderLineItemEventHandler {

	private final OrderLineItemRepository orderLineItemRepository;
	private final MenuRepository menuRepository;

	public OrderLineItemEventHandler(OrderLineItemRepository orderLineItemRepository,
		MenuRepository menuRepository) {
		this.orderLineItemRepository = orderLineItemRepository;
		this.menuRepository = menuRepository;
	}

	@EventListener
	public void createOrderLineItem(OrderCreateEvent orderCreateEvent) {
		OrderLineItems orderLineItems = toOrderLineItems(orderCreateEvent.getOrderLineItemRequests(),
			orderCreateEvent.getOrder());
		orderLineItems.stream().forEach(orderLineItemRepository::save);
	}

	private OrderLineItems toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
		return orderLineItemRequests.stream()
			.map(orderLineItemRequest -> {
				Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
					.orElseThrow(() -> new OrderException("메뉴가 존재하지 않아 주문 생성할 수 없습니다."));
				return orderLineItemRequest.toOrderLineItem(menu, order);
			}).collect(collectingAndThen(toList(), OrderLineItems::new));
	}

}
