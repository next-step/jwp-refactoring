package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.menu.domain.Menu;

@Embeddable
public class OrderLineItems {
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderLineItem> orderLineItems = new ArrayList<>();

	protected OrderLineItems() {
	}

	public OrderLineItems(List<OrderLineItem> orderLineItems) {
		Set<Menu> menuSet = orderLineItems.stream()
			.map(OrderLineItem::getMenu)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
		if (menuSet.isEmpty() || menuSet.size() != orderLineItems.size()) {
			throw new IllegalArgumentException("메뉴는 누락되거나 중복될 수 없습니다.");
		}

		this.orderLineItems = orderLineItems;
	}

	public List<OrderLineItem> getList() {
		return orderLineItems;
	}
}
