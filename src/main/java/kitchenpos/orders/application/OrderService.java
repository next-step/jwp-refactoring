package kitchenpos.orders.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.orders.dao.OrderDao;
import kitchenpos.orders.dao.OrderLineItemDao;
import kitchenpos.orders.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.orders.domain.OrderLineItem;
import kitchenpos.orders.domain.OrderLineItems;
import kitchenpos.orders.domain.OrderStatus;
import kitchenpos.orders.domain.Orders;
import kitchenpos.orders.dto.OrderLineItemRequest;
import kitchenpos.orders.dto.OrderRequest;
import kitchenpos.orders.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;

@Service
public class OrderService {
	private final MenuDao menuDao;
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public OrderService(
		final MenuDao menuDao,
		final OrderDao orderDao,
		final OrderTableDao orderTableDao
	) {
		this.menuDao = menuDao;
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
		OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
		OrderLineItems orderLineItems = orderRequestToOrderItem(orderRequest.getOrderLineItems());

		if(orderTable.isEmpty()){
			throw new IllegalArgumentException();
		}
		Orders savedOrder = orderDao.save(new Orders(orderTable, OrderStatus.COOKING.name()));
		savedOrder.add(orderLineItems);

        return OrderResponse.of(savedOrder);

    }

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrderResponse> list() {
		return orderDao.findAll().stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Orders savedOrder = orderDao.findById(orderId).orElseThrow(IllegalArgumentException::new);
        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

		return OrderResponse.of(savedOrder);
    }


	private OrderLineItems orderRequestToOrderItem(List<OrderLineItemRequest> orderLineItemsRequests) {
		if(orderLineItemsRequests.isEmpty()) throw new IllegalArgumentException();
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		for (OrderLineItemRequest orderLineItemRequest : orderLineItemsRequests) {
			Menu menu = menuDao.findById(orderLineItemRequest.getMenuId()).orElseThrow(IllegalArgumentException::new);
			orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
		}

		return new OrderLineItems(orderLineItems);
	}
}
