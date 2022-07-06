package ktichenpos.menu.dto;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ktichenpos.menu.domain.Menu;
import ktichenpos.menu.domain.MenuGroup;
import ktichenpos.menu.domain.MenuProduct;
import ktichenpos.menu.domain.MenuProducts;

public class MenuDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;
    private List<MenuProductDto> menuProductDtos = new ArrayList<>();

    private MenuDto() {
    }

    private MenuDto(Long id, String name, BigDecimal price, MenuGroup menuGroup,
                    List<MenuProductDto> menuProductDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProductDtos = menuProductDtos;
    }

    public static MenuDto of(Menu menu) {
        return new MenuDto(menu.getId()
                , menu.getName()
                , menu.getPrice()
                , menu.getMenuGroup()
                , menu.getMenuProducts().toList()
                .stream()
                .map(MenuProductDto::of)
                .collect(toList()));
    }

    public Menu toMenu() {
        return new Menu(name, price, menuGroup, menuProductList());
    }

    public MenuProducts menuProducts() {
        return new MenuProducts(menuProductList());
    }

    private List<MenuProduct> menuProductList() {
        return menuProductDtos.stream()
                .map(menuProductDto -> new MenuProduct(menuProductDto.getProductId(), menuProductDto.getQuantity()))
                .collect(toList());
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }
}
