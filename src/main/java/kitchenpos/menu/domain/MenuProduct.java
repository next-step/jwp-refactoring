package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
	private Menu menu;

	private Long productId;
	@Embedded
	private Quantity quantity;

	protected MenuProduct() {}

	private MenuProduct(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = Quantity.of(quantity);
	}

	public static MenuProduct of(Long productId, int quantity) {
		return new MenuProduct(productId, quantity);
	}

	public void updateMenu(Menu menu) {
		this.menu = menu;
	}

	public Price getTotalPrice(Price productPrice) {
		return productPrice.multiply(quantity);
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Long getProductId() {
		return productId;
	}

	public Quantity getQuantity() {
		return quantity;
	}

}
