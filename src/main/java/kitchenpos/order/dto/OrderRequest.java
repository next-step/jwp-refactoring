package kitchenpos.order.dto;

import io.micrometer.core.instrument.util.StringUtils;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;

import java.util.*;
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

	public Orders toEntity(OrderTable orderTable, List<Menu> menus) {
		OrderStatus orderStatus = StringUtils.isBlank(this.orderStatus) ? OrderStatus.COOKING
				: OrderStatus.valueOf(this.orderStatus);

		return new Orders(orderTable, orderStatus.name(), toOrderLineItems(menus));
	}

	private List<OrderLineItem> toOrderLineItems(List<Menu> menus) {
		Map<Long, Menu> menuInfo = menus.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));

		return this.orderLineItems.stream()
				.filter(orderLineItem -> menuInfo.containsKey(orderLineItem.getMenuId()))
				.map(orderLineItem -> {
					Menu menu = menuInfo.get(orderLineItem.getMenuId());
					return new OrderLineItem(menu, orderLineItem.getQuantity());
				})
				.collect(Collectors.toList());
	}
}
