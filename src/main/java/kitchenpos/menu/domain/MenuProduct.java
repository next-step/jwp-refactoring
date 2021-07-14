package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
	@Column(length = 20, nullable = false)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
	private Product product;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {

	}

	public MenuProduct(Product product, Quantity quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public MenuProduct(Long seq, Product product, Quantity quantity) {
		this(product, quantity);
		this.seq = seq;
	}

	public MenuProduct(Menu menu, Product product, Quantity quantity) {
		this(product, quantity);
		this.menu = menu;
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}

	public Quantity getQuantity() {
		return quantity;
	}

	public Price getMenuProductPrice() {
		return new Price(product.getPrice().multiply(BigDecimal.valueOf(quantity.value())));
	}
}
