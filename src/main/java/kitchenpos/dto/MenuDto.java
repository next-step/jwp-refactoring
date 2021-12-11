package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;

public class MenuDto {
    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    protected MenuDto() { 
    }

    private MenuDto(Long id, String name, Price price, Long menuGroupId, List<MenuProductDto> menuProducts) { 
        this.id = id;
        this.name = name; 
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto of(Long id, String name, Price price, Long menuGroupId, List<MenuProductDto> menuProducts) { 
        return new MenuDto(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuDto of(Menu menu) {         
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), menu.getMenuProducts().stream()
                                                                                                            .map(MenuProductDto::of)
                                                                                                            .collect(Collectors.toList()));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }
    public List<MenuProductDto> getMenuProducts() {
        return this.menuProducts;
    }

    public Menu toMenu() {
        return Menu.of(this.id, this.name, this.price, null, null);
    }
}
