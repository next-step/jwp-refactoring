package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Embedded
	private Price price;
	@ManyToOne
	private MenuGroup menuGroup;
	@Embedded
	private MenuProducts menuProducts = new MenuProducts();

	protected Menu() {
	}

	private Menu(String name, Integer price, MenuGroup menuGroup) {
		validateNull(price, menuGroup);
		this.name = name;
		this.price = new Price(price);
		this.menuGroup = menuGroup;
	}

	private void validateNull(Integer price, MenuGroup menuGroup) {
		if (Objects.isNull(price)) {
			throw new IllegalArgumentException("가격 정보가 없습니다.");
		}

		if (Objects.isNull(menuGroup)) {
			throw new IllegalArgumentException("메뉴 그룹 정보가 없습니다.");
		}
	}

	private void validatePrice() {
		if (price.isGreaterThan(menuProducts.sumPrices())) {
			throw new IllegalArgumentException("메뉴 가격은 원래 상품 가격의 합보다 클 수 없습니다.");
		}
	}

	public static MenuBuilder builder() {
		return new MenuBuilder();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price.value();
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public List<MenuProduct> getMenuProducts() {
		return menuProducts.getList();
	}

	public void setMenuProducts(List<MenuProduct> menuProducts) {
		this.menuProducts = new MenuProducts(id, menuProducts);
		validatePrice();
	}

	public static final class MenuBuilder {
		private String name;
		private Integer price;
		private MenuGroup menuGroup;

		private MenuBuilder() {
		}

		public MenuBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuBuilder price(Integer price) {
			this.price = price;
			return this;
		}

		public MenuBuilder menuGroup(MenuGroup menuGroup) {
			this.menuGroup = menuGroup;
			return this;
		}

		public Menu build() {
			return new Menu(name, price, menuGroup);
		}
	}
}
