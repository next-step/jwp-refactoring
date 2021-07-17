package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
	@Column(nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
	@Column(nullable = false)
	private Menu menu;

	private int quantity;
}
