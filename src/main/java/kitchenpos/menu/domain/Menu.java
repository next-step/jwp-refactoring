package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.dto.MenuProductDto;

@Table(name = "menu")
@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Name name;

	@Embedded
	private Price price;

	private Long menuGroupId;

	@Embedded
	private MenuProducts menuProducts;

	protected Menu() {
	}

	private Menu(
		Long id,
		Name name,
		Price price,
		Long menuGroupId,
		List<MenuProductDto> menuProductDtos,
		MenuValidator validator
	) {
		validator.validateMenuGroupExist(menuGroupId);
		validator.validateProductsExist(menuProductDtos);
		validator.validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(price, menuProductDtos);

		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = MenuProducts.from(menuProductDtos.stream()
			.map(dto -> MenuProduct.of(dto.getProductId(), Quantity.from(dto.getQuantity())))
			.collect(Collectors.toList()));
	}

	public static Menu of(
		Long id,
		Name name,
		Price price,
		Long menuGroupId,
		List<MenuProductDto> menuProductDtos,
		MenuValidator validator
	) {
		return new Menu(id, name, price, menuGroupId, menuProductDtos, validator);
	}

	public static Menu of(
		Name name,
		Price price,
		Long menuGroupId,
		List<MenuProductDto> menuProductDtos,
		MenuValidator validator
	) {
		return new Menu(null, name, price, menuGroupId, menuProductDtos, validator);
	}

	public Long getId() {
		return id;
	}

	public Name getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public MenuProducts getMenuProducts() {
		return menuProducts;
	}
}
