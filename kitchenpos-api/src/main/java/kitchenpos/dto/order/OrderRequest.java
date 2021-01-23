package kitchenpos.dto.order;


import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderRequest {
	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	public OrderRequest() {
	}
	public OrderRequest(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public Set<Long> menuIds() {
		final List<Long> menuIds = orderLineItems.stream()
				.map(orderLineItem -> orderLineItem.getMenuId())
				.collect(Collectors.toList());
		return new HashSet<>(menuIds);
	}

	public Orders toEntity(OrderTable orderTable) {
		OrderStatus orderStatus = StringUtils.isBlank(this.orderStatus) ? OrderStatus.COOKING
				: OrderStatus.valueOf(this.orderStatus);

		return new Orders(orderTable, orderStatus.name());
	}

	public List<OrderLineItem> toOrderLineItems(Orders order, List<Menu> menus) {
		Map<Long, Menu> menuInfo = menus.stream()
				.collect(Collectors.toMap(Menu::getId, Function.identity()));

		return this.orderLineItems.stream()
				.filter(orderLineItem -> menuInfo.containsKey(orderLineItem.getMenuId()))
				.map(orderLineItem -> {
					Menu menu = menuInfo.get(orderLineItem.getMenuId());
					return new OrderLineItem(order, menu, orderLineItem.getQuantity());
				})
				.collect(Collectors.toList());
	}
}
