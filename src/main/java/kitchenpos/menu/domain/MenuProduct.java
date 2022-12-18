package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public static List<MenuProduct> of(Menu menu, List<Product> products) {
		return products.stream()
					   .map(product -> new MenuProduct(menu, product, 1))
					   .collect(Collectors.toList());
	}

	public Long getProductId() {
		return product.getId();
	}

	public long getQuantity() {
		return quantity;
	}

	public Money totalPrice() {
		return product.getPrice()
					  .multiply(quantity);
	}
}
