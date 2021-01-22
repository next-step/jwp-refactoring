package kitchenpos.menu.domain;

import kitchenpos.common.BaseSeqEntity;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;

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

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = new Quantity(quantity);
	}

	Price getQuantityPrice() {
		return product.getPrice().multiply(quantity);
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
