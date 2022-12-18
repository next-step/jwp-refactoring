package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		changeMenu(menu);
		this.product = product;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return product.getId();
	}

	public long getQuantity() {
		return quantity;
	}

	public Money totalPrice() {
		return product.getPrice().multiply(quantity);
	}

	public void changeMenu(Menu newMenu) {
		if (Objects.nonNull(menu)) {
			menu.getMenuProducts().remove(this);
		}
		menu = newMenu;
		if (!menu.getMenuProducts().contains(this)) {
			menu.getMenuProducts().add(this);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuProduct that = (MenuProduct)o;
		return seq.equals(that.seq);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}
}
