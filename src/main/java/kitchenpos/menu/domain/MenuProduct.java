package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

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
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct of(Product product, Quantity quantity) {
		return new MenuProduct(product, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menu.getId();
	}
	public Product getProduct() {
		return product;
	}

	public long getProductId() {
		return product.getId();
	}


	public long getQuantity() {
		return quantity.value();
	}
}
