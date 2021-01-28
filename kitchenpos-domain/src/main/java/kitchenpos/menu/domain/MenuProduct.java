package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kitchenpos.common.BaseEntity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	private MenuProduct(final Long seq, final Menu menu, final Product product, final long quantity) {
		this.id = seq;
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct of(final Menu menu, final Product product, final long quantity) {
		return new MenuProduct(null, menu, product, quantity);
	}

	public Long getId() {
		return id;
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

	public long menuProductPrice() {
		return this.product.price() * quantity;
	}
}
