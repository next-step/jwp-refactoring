package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long seq;

	@Column(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
	private Long menuId;

	@Column(name = "product_id", nullable = false, columnDefinition = "bigint(20)")
	private Long productId;

	@Embedded
	@Column(nullable = false, columnDefinition = "bigint(20)")
	private Quantity quantity;

	public MenuProduct() {
	}

	public MenuProduct(Long productId, Quantity quantity) {
		this(null, null, productId, quantity);
	}

	public MenuProduct(Long menuId, Long productId, Quantity quantity) {
		this(null, menuId, productId, quantity);
	}

	public MenuProduct(Long seq, Long menuId, Long productId, Quantity quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public long getQuantityValue() {
		return quantity.value();
	}
}
