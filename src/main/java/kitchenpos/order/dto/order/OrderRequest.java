package kitchenpos.order.dto.order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.order.domain.order.OrderLineItem;

public class OrderRequest {

	private Long id;
	private Long orderTableId;
	private String orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	protected OrderRequest() {
	}

	public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public void setOrderTableId(final Long orderTableId) {
		this.orderTableId = orderTableId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(final String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}

	public void setOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
		this.orderLineItems = orderLineItems;
	}

	public List<Long> getMenuIds() {
		if (CollectionUtils.isEmpty(this.orderLineItems)) {
			throw new IllegalArgumentException("주문 항목 요청 목록이 없습니다");
		}
		return this.orderLineItems.stream()
			.map(OrderLineItemRequest::getMenuId)
			.collect(Collectors.toList());
	}

	public List<OrderLineItem> toOrderLineItems(final List<Menu> menus) {
		Map<Long, Menu> menuById = menus.stream()
			.collect(Collectors.toMap(Menu::getId, Function.identity()));

		validateOrderLineItemRequestExists(menuById, this.orderLineItems);

		return this.orderLineItems.stream()
			.map(orderLineItemRequest ->
				orderLineItemRequest.toOrderLineItem(menuById.get(orderLineItemRequest.getMenuId())))
			.collect(Collectors.toList());
	}

	private void validateOrderLineItemRequestExists(final Map<Long, Menu> menuById,
		final List<OrderLineItemRequest> orderLineItemRequests) {
		orderLineItemRequests.stream()
			.filter(orderLineItemRequest -> !menuById.containsKey(orderLineItemRequest.getMenuId()))
			.findAny()
			.ifPresent(orderLineItemRequest -> {
				throw new IllegalArgumentException(String.format("요청한 %d에 대한 메뉴 정보가 없습니다.",
					orderLineItemRequest.getMenuId()));
			});
	}
}
