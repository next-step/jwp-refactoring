package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(name = "menu_id")
	private Long menuId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	private long quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Long menuId, Product product, long quantity) {
		this.menuId = menuId;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct create(Long menuId, Product product, long quantity) {
		return new MenuProduct(menuId, product, quantity);
	}

	public BigDecimal getPrice() {
		BigDecimal decimalQuantity = BigDecimal.valueOf(this.quantity);
		return this.product
			.getPrice()
			.multiply(decimalQuantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Long getProductId() {
		return this.product.getId();
	}

	public Long getMenuId() {
		return menuId;
	}

	public Product getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}

}
