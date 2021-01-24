package kitchenpos.menu.domain;

import kitchenpos.common.entity.BaseSeqEntity;
import kitchenpos.common.entity.Price;
import kitchenpos.common.entity.Quantity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct extends BaseSeqEntity {

	@Column(name = "menu_id", nullable = false, insertable = false, updatable = false)
	private Long menuId;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Product product, long quantity) {
		this.product = product;
		this.quantity = new Quantity(quantity);
	}

	Price getQuantityPrice() {
		return product.getPrice().multiply(quantity);
	}

	public Product getProduct() {
		return product;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MenuProduct)) return false;
		if (!super.equals(o)) return false;
		MenuProduct that = (MenuProduct) o;
		return Objects.equals(menuId, that.menuId) &&
				Objects.equals(product, that.product) &&
				Objects.equals(quantity, that.quantity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), menuId, product, quantity);
	}
}
