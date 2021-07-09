package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_to_menu_product"), nullable = false)
	private Product product;

	@Embedded
	private Quantity quantity;

	protected MenuProduct() {

	}

	public MenuProduct(Long seq, Product product, Quantity quantity) {
		this(product, quantity);
		this.seq = seq;
	}

	public MenuProduct(Product product, Quantity quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Long getSeq() {
		return seq;
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
