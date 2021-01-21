package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
		this.seq = seq;
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}
}
