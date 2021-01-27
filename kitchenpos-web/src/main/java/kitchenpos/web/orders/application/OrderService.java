package kitchenpos.web.orders.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.web.menu.repository.MenuRepository;
import kitchenpos.domain.orders.domain.OrderLineItem;
import kitchenpos.domain.orders.domain.OrderLineItems;
import kitchenpos.domain.orders.domain.OrderStatus;
import kitchenpos.domain.orders.domain.Orders;
import kitchenpos.web.orders.dto.OrderLineItemRequest;
import kitchenpos.web.orders.dto.OrderRequest;
import kitchenpos.web.orders.dto.OrderResponse;
import kitchenpos.web.orders.repository.OrderRepository;
import kitchenpos.web.orders.repository.OrderTableRepository;
import kitchenpos.domain.table.domain.OrderTable;

@Service
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(
		final MenuRepository menuRepository,
		final OrderRepository orderRepository,
		final OrderTableRepository orderTableRepository
	) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
		OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
		OrderLineItems orderLineItems = orderRequestToOrderItem(orderRequest.getOrderLineItems());

		if(orderTable.isEmpty()){
			throw new IllegalArgumentException();
		}
		Orders savedOrder = orderRepository.save(new Orders(orderTable, OrderStatus.COOKING.name()));
		savedOrder.add(orderLineItems);

        return OrderResponse.of(savedOrder);

    }

	@Transactional(readOnly = true)
    public List<OrderResponse> list() {
		return orderRepository.findAll().stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Orders savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

		return OrderResponse.of(savedOrder);
    }


	private OrderLineItems orderRequestToOrderItem(List<OrderLineItemRequest> orderLineItemsRequests) {
		if(orderLineItemsRequests.isEmpty()) throw new IllegalArgumentException();
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		for (OrderLineItemRequest orderLineItemRequest : orderLineItemsRequests) {
			Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
			orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
		}

		return new OrderLineItems(orderLineItems);
	}
}
