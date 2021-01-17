package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	protected MenuProduct() {
	}
	private long quantity;

	public MenuProduct(Product product, long quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(final Long seq) {
		this.seq = seq;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(final long quantity) {
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
}
