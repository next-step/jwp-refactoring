package kitchenpos.domain;

import kitchenpos.common.BaseSeqEntity;

import javax.persistence.*;

@Entity
@Table(name = "menu_product")
public class MenuProduct extends BaseSeqEntity {

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "quantity", nullable = false)
	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
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
