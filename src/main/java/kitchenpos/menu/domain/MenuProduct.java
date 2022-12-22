package kitchenpos.menu.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
	@JoinColumn(name = "product_id")
	private Product product;

	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Long id, Product product, long quantity) {
		this.seq = id;
		this.product = product;
		this.quantity = quantity;
	}

	public MenuProduct(Product product, long quantity) {
		this(null, product, quantity);
	}

	public static List<MenuProduct> of(List<Product> products) {
		Map<Product, Integer> productsCount = products.stream()
												.collect(
													Collectors.toMap(
														Function.identity(), it -> 1, Integer::sum));
		return products.stream()
					   .map(product -> new MenuProduct(product, productsCount.get(product)))
					   .collect(Collectors.toList());
	}

	public Long getProductId() {
		return product.getId();
	}

	public Product getProduct() {
		return product;
	}

	public long getQuantity() {
		return quantity;
	}

	public Money totalPrice() {
		return product.getPrice()
					  .multiply(quantity);
	}
}
