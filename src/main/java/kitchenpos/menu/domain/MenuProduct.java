package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "menuId")
	private Menu menu;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "productId")
	private Product product;

	private long quantity;

	public MenuProduct() {
	}

	public MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public long getQuantity() {
		return quantity;
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}
}
