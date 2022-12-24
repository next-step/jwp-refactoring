package ktichenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long seq;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
	private Menu menu;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
	private Product product;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Product product, Quantity quantity) {
		Assert.notNull(product, "상품은 필수입니다.");
		Assert.notNull(quantity, "수량은 필수입니다.");
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct of(Product product, Quantity quantity) {
		return new MenuProduct(product, quantity);
	}

	public long seq() {
		return seq;
	}

	public Product product() {
		return product;
	}

	public long productId() {
		return product.id();
	}

	public Price price() {
		return quantity.multiply(product.price());
	}

	public long quantity() {
		return quantity.value();
	}

	public void updateMenu(Menu menu) {
		this.menu = menu;
	}
}
