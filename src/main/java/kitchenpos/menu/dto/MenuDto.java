package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;

public class MenuDto {
	private Long id;
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductDto> menuProducts;

	public MenuDto() {
	}

	public MenuDto(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductDto> getMenuProducts() {
		return menuProducts;
	}

	public static MenuDto from(Menu menu) {
		MenuDto dto = new MenuDto();
		dto.id = menu.getId();
		dto.name = menu.getName().getValue();
		dto.price = menu.getPrice().getValue();
		dto.menuGroupId = menu.getMenuGroup().getId();
		dto.menuProducts = menu.getMenuProducts()
			.getValues()
			.stream()
			.map(MenuProductDto::from)
			.collect(Collectors.toList());
		return dto;
	}
}
