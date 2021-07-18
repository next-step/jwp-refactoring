package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
	private Menu menu;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_product"))
	private Product product;

	private long quantity;

	public MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return product.getPrice().getPrice();
	}

	public long getQuantity() {
		return quantity;
	}

	public BigDecimal getTotalPrice() {
		return product.getTotalPrice(quantity);
	}
}
