package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductResponse;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
	private Menu menu;

	@Column(nullable = false)
	private Long productId;

	private long quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Menu menu, Long productId, Long quantity) {
		this.menu = menu;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProduct of(Menu menu, Long productId, Long quantity) {
		return new MenuProduct(menu, productId, quantity);
	}

	public MenuProductResponse toResDto() {
		return MenuProductResponse.of(seq, productId, quantity);
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

	public long getQuantity() {
		return quantity;
	}

	public Price calculateMenuProductPrice(BigDecimal price) {
		return Price.from(price.multiply(BigDecimal.valueOf(quantity)).intValue());
	}
}
