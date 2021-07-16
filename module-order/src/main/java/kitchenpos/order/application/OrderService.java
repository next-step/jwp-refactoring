package kitchenpos.order.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.event.OrderCreateEvent;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final TableRepository tableRepository;
	private final ApplicationEventPublisher eventPublisher;

	public OrderService(
		final OrderRepository orderRepository,
		final TableRepository tableRepository,
		final ApplicationEventPublisher eventPublisher
	) {
		this.orderRepository = orderRepository;
		this.tableRepository = tableRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		final OrderTable orderTable = tableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new OrderException("주문 테이블이 존재하지 않아 주문 할 수 없습니다."));
		Order order = orderRequest.toOrder(orderTable);
		eventPublisher.publishEvent(new OrderCreateEvent(order, orderRequest.getOrderLineItemRequests()));
		final Order savedOrder = orderRepository.save(order);
		return OrderResponse.of(savedOrder);
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();
		return OrderResponse.of(orders);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId,
		final OrderStatusChangeRequest orderStatusChangeRequest) {
		final Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
		savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus()));
		return OrderResponse.of(savedOrder);
	}

}
