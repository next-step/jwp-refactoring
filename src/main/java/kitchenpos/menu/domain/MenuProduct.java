package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.Price;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Menu menu, Product product, Long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct of(Menu menu, Product product, Long quantity) {
		return new MenuProduct(menu, product, quantity);
	}

	public MenuProductResponse toResDto() {
		return MenuProductResponse.of(seq, product, quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public long getQuantity() {
		return quantity;
	}

	public Price getTotalPrice() {
		return Price.from(product
			.getPrice()
			.multiply(BigDecimal.valueOf(quantity)));
	}
}
