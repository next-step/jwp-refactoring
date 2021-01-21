package kitchenpos.menu.domain;

import kitchenpos.common.QuantityEntity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct extends QuantityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private Long quantity;
	
	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(final Long quantity) {
		this.quantity = quantity;
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}

	public BigDecimal sumOfPrice() {
		return this.product.getPrice().multiply(new BigDecimal(this.quantity));
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
