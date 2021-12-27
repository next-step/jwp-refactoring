package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;

@Table(name = "menu_product")
@Entity
public class MenuProduct {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long seq;

	private Long productId;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	public static MenuProduct of(Long seq, Long productId, Quantity quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.seq = seq;
		menuProduct.productId = productId;
		menuProduct.quantity = quantity;
		return menuProduct;
	}

	public static MenuProduct of(Long productId, Quantity quantity) {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.productId = productId;
		menuProduct.quantity = quantity;
		return menuProduct;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getProductId() {
		return productId;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
