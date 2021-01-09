package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderItem;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final OrderLineItemRepository orderLineItemRepository;

	public OrderService(
		final MenuRepository menuRepository,
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository,
		final OrderLineItemRepository orderLineItemRepository
	) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.orderLineItemRepository = orderLineItemRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		final List<Long> menuIds = orderRequest.getMenuIds();

		if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
			throw new NotFoundException("요청된 메뉴 정보 중 데이터베이스에 없는 정보가 존재합니다.");
		}

		final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new NotFoundException("주문테이블 정보를 찾을 수 없습니다."));

		final Order savedOrder = orderRepository.save(Order.create(orderTable));
		addOrderItem(orderRequest, savedOrder);
		return OrderResponse.of(savedOrder);
	}

	private void addOrderItem(OrderRequest orderRequest, Order savedOrder) {
		List<OrderItem> orderItems = orderRequest.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			Menu menu = menuRepository.findById(orderItem.getMenuId())
				.orElseThrow(() -> new NotFoundException("메뉴 정보를 찾을 수 없습니다."));
			OrderLineItem orderLineItem = OrderLineItem.create(savedOrder.getId(), menu, orderItem.getQuantity());
			savedOrder.addOrderLineItems(orderLineItemRepository.save(orderLineItem));
		}
	}

	public List<OrderResponse> findAll() {
		final List<Order> orders = orderRepository.findAllFetch();
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order savedOrder = orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException("주문 정보를 찾을 수 없습니다."));

		savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
		return OrderResponse.of(savedOrder);
	}
}
