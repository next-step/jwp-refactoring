package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "quantity")
	private long quantity;

	protected MenuProduct() {
	}

	public MenuProduct(Product product, long quantity) {
		if (product == null) {
			throw new IllegalArgumentException("상품이 없어 메뉴상품을 구성할 수 없습니다");
		}
		this.product = product;
		this.quantity = quantity;
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

	public long getQuantity() {
		return quantity;
	}

	public void changeMenu(Menu menu) {
		this.menu = menu;
	}

	public Price getTotalPrice() {
		return new Price(product.getPrice().multiply(quantity));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MenuProduct that = (MenuProduct)o;
		return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menu.getId(),
			that.menu.getId()) && Objects.equals(product.getId(), that.product.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq, menu.getId(), product.getId(), quantity);
	}
}
