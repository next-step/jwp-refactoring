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
		setMenu(menu);
		setProduct(product);
		this.quantity = quantity;
	}

	public Long getProductId() {
		return product.getId();
	}

	public long getQuantity() {
		return quantity;
	}

	public void setMenu(Menu newMenu) {
		if (Objects.nonNull(menu)) {
			menu.getMenuProducts().remove(this);
		}
		menu = newMenu;
		if (!menu.getMenuProducts().contains(this)) {
			menu.getMenuProducts().add(this);
		}
	}

	public void setProduct(Product newProduct) {
		this.product = newProduct;
	}

	public Money getPurchasePrice() {
		return product.getPrice().multiply(quantity);
	}
}
