package kitchenpos.menu.domain;

import kitchenpos.common.QuantityEntity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct extends QuantityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(name = "menu_id")
	private Long menuId;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@Column
	private Long quantity;
	
	protected MenuProduct() {
	}

	public static MenuProduct of(long menuId, Product product, Long quantity) {
		return new MenuProduct(menuId, product, quantity);
	}

	public MenuProduct(long menuId, Product product, long quantity) {
		this.menuId = menuId;
		this.product = product;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Product getProduct() {
		return product;
	}

	public BigDecimal sumOfPrice() {
		return this.product.getPrice().multiply(new BigDecimal(this.quantity));
	}

}
