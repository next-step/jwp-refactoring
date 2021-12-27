package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@Column(columnDefinition = "bigint(20)")
	private Long seq;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
	private Menu menu;

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

	public MenuProduct(Menu menu, Long productId, Quantity quantity) {
		this(null, menu, productId, quantity);
	}

	public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
		this.seq = seq;
		this.menu = menu;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public long getMenuId() {
		return this.menu.getId();
	}

	public long getQuantityValue() {
		return quantity.value();
	}
}
