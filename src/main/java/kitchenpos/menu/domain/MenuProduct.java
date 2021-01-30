package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;
	@ManyToOne
	private Menu menu;
	@ManyToOne
	private Product product;
	private long quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Menu menu, Product product, long quantity) {
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;

		validate();
	}

	private void validate() {
		if (Objects.isNull(product)) {
			throw new IllegalArgumentException("상품 정보가 없습니다.");
		}
	}

	public static MenuProductBuilder builder() {
		return new MenuProductBuilder();
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

	public long getQuantity() {
		return quantity;
	}

	public int calculatePrice() {
		return product.getPrice().multiply(quantity).value();
	}

	public static final class MenuProductBuilder {
		private Menu menu;
		private Product product;
		private long quantity;

		private MenuProductBuilder() {
		}

		public MenuProductBuilder menu(Menu menu) {
			this.menu = menu;
			return this;
		}

		public MenuProductBuilder product(Product product) {
			this.product = product;
			return this;
		}

		public MenuProductBuilder quantity(long quantity) {
			this.quantity = quantity;
			return this;
		}

		public MenuProduct build() {
			return new MenuProduct(menu, product, quantity);
		}
	}
}
