package kitchenpos.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Quantity;

@Entity
public class OrderLineItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	private Menu menu;

	@Embedded
	private Quantity quantity;

	//todo protected
	public OrderLineItem() {

	}

	public OrderLineItem(Menu menu, Quantity quantity) {
		this.menu = menu;
		this.quantity = quantity;
	}

	public OrderLineItem(Long id, Menu menu, Quantity quantity) {
		this(menu, quantity);
		this.seq = id;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(final Long seq) {
		this.seq = seq;
	}

}
