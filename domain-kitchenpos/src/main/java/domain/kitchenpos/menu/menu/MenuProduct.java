package domain.kitchenpos.menu.menu;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import domain.kitchenpos.menu.product.Product;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		validate(quantity);
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

	public Long getMenuId() {
		return this.menu.getId();
	}

	public Long getProductId() {
		return this.product.getId();
	}

	private void validate(long quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("수량은 0보다 적을 수 없습니다.");
		}
	}

	public BigDecimal getPrice() {
		return this.getProduct().getPrice().multiply(BigDecimal.valueOf(this.quantity));
	}
}
