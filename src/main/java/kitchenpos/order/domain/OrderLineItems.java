package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {
	@OneToMany(mappedBy = "orderId", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	public OrderLineItems(Long orderId, List<OrderLineItem> orderLineItems) {
		validate(orderLineItems);
		orderLineItems.forEach(orderLineItem -> orderLineItem.setOrderId(orderId));
		this.orderLineItems = orderLineItems;
	}

	private void validate(List<OrderLineItem> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException("주문 항목이 비어있습니다.");
		}
	}

	public List<OrderLineItem> getList() {
		return orderLineItems;
	}
}
