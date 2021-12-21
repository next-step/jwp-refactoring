package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Table(name = "menu_product")
@Entity
public class MenuProduct {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long seq;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id")
	private Product product;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	public static MenuProduct of(Product product, Quantity quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.product = product;
		menuProduct.quantity = quantity;
		return menuProduct;
	}

	public Long getSeq() {
		return seq;
	}

	public Product getProduct() {
		return product;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
