package kitchenpos.order.domain;

import javax.persistence.Entity;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	private Long seq;

	private Order order;

	private Menu menu;

	private int quantity;
}
