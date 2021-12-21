package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@Service(value = "ToBeOrderService")
@Transactional(readOnly = true)
public class OrderService {
	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(OrderRepository orderRepository, MenuRepository menuRepository,
		OrderTableRepository orderTableRepository) {
		this.orderRepository = orderRepository;
		this.menuRepository = menuRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderDto create(OrderRequest request) {
		OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
			.orElseThrow(IllegalArgumentException::new);
		OrderLineItems orderLineItems = OrderLineItems.of(
			request.getOrderLineItems()
				.stream()
				.map(ol -> {
					Menu menu = menuRepository.findById(ol.getMenuId()).orElseThrow(IllegalArgumentException::new);
					Quantity quantity = Quantity.of(ol.getQuantity());
					return OrderLineItem.of(menu, quantity);
				}).collect(Collectors.toList()));

		Order order = orderRepository.save(Order.of(orderTable, orderLineItems));
		return OrderDto.of(order);
	}

	public List<OrderDto> list() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream()
			.map(OrderDto::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderDto changeOrderStatus(Long id, OrderRequest request) {
		Order order = orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
		order.changeOrderStatus(request.getOrderStatus());
		return OrderDto.of(order);
	}
}
