package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final MenuRepository menuRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(final OrderRepository orderRepository,
		final MenuRepository menuRepository,
		final OrderTableRepository orderTableRepository) {

		this.orderRepository = orderRepository;
		this.menuRepository = menuRepository;
		this.orderTableRepository = orderTableRepository;
	}

	public OrderResponse create(final OrderRequest orderRequest) {
		OrderTable orderTable = getOrderTableById(orderRequest.getOrderTableId());
		Order order = Order.create(orderTable.getId());
		List<OrderLineItem> orderLineItemList = getOrderLineItems(orderRequest.getOrderLineItems());
		order.addOrderLineItems(orderLineItemList);
		return new OrderResponse(orderRepository.save(order));
	}

	private OrderTable getOrderTableById(Long orderTableId) {
		OrderTable orderTable = orderTableRepository.findById(orderTableId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "주문 테이블을 찾을 수 없습니다"));
		if (orderTable.isEmpty()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "빈 주문 테이블입니다.");
		}
		return orderTable;
	}

	private List<OrderLineItem> getOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
		return orderLineItems.stream().map(this::getOrderItem).collect(Collectors.toList());
	}

	private OrderLineItem getOrderItem(OrderLineItemRequest request) {
		Menu menu = menuRepository.findById(request.getMenuId())
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다"));
		return OrderLineItem.create(menu.getId(), request.getQuantity());
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
