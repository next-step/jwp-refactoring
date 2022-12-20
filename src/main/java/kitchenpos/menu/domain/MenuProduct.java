package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

	@Column(nullable = false, updatable = false)
	@JoinColumn(nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
	private Long productId;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Long productId, Quantity quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProduct of(Long productId, Quantity quantity) {
		return new MenuProduct(productId, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menu.getId();
	}
	public Long getProductId() {
		return productId;
	}
	public long getQuantity() {
		return quantity.value();
	}
}
