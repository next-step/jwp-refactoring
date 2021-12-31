package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderValidator orderValidator;

	public OrderService(final OrderRepository orderRepository,
		final OrderValidator orderValidator) {

		this.orderRepository = orderRepository;
		this.orderValidator = orderValidator;
	}

	public OrderResponse create(final OrderRequest orderRequest) {
		orderValidator.validateCreate(orderRequest);
		Order order = Order.create(orderRequest.getOrderTableId());
		List<OrderLineItem> orderLineItemList = getOrderLineItems(orderRequest.getOrderLineItems());
		order.addOrderLineItems(orderLineItemList);
		return new OrderResponse(orderRepository.save(order));
	}

	private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
		return orderLineItems.stream().map(this::getOrderItem).collect(Collectors.toList());
	}

	private OrderLineItem getOrderItem(OrderLineItemRequest request) {
		return OrderLineItem.create(request.getMenuId(), request.getQuantity());
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> list() {
		final List<Order> orderList = orderRepository.findAll();
		return orderList.stream().map(OrderResponse::new).collect(Collectors.toList());
	}

	public OrderResponse changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
		Order order = getById(orderId);
		order.updateStatus(request.getOrderStatus());
		return new OrderResponse(order);
	}

	private Order getById(Long id) {
		return orderRepository.findById(id)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "주문을 찾을 수 없습니다"));
	}
}
