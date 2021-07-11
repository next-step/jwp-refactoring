package kitchenpos.menu.dto;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.Menu;

import static java.util.stream.Collectors.toList;

public class MenuDto {
    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    public MenuDto() { }

    public MenuDto(String name, Long price, Long menuGroupId, List<MenuProductDto> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public MenuDto(Long id, String name, Long price, Long menuGroupId, List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto of(Menu menu) {
        List<MenuProductDto> menuProductDtos = menu.getMenuProducts()
                                                   .getData()
                                                   .stream()
                                                   .map(MenuProductDto::of)
                                                   .collect(toList());
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroup().getId(), menuProductDtos);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(id, menuDto.id) && Objects.equals(name, menuDto.name)
            && Objects.equals(price, menuDto.price) && Objects.equals(menuGroupId,
                                                                      menuDto.menuGroupId)
            && Objects.equals(menuProducts, menuDto.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
