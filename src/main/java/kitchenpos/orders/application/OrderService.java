package kitchenpos.orders.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.orders.domain.Order;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderLineItems;
import kitchenpos.orders.domain.OrderRepository;
import kitchenpos.orders.domain.OrderValidator;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;

	public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
	}

	@Transactional
	public Order create(final OrderRequest orderRequest) {
		Order order = orderRequest.toOrder();
		orderValidator.validateOrderCreate(order);
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<Order> list() {
		return orderRepository.findAll();
	}

	@Transactional
	public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order order = findOrderById(orderId);
		order.changeOrderStatus(orderRequest.getOrderStatus());
		return order;
	}

	@Transactional(readOnly = true)
	public Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
	}

}
