package menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import menu.domain.Menu;
import menu.domain.MenuGroup;
import menu.domain.MenuProduct;

public class MenuRequestDto {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequestDto> menuProducts;

    public MenuRequestDto() {
    }

    public MenuRequestDto(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequestDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequestDto> getMenuProducts() {
        return menuProducts;
    }

    public Menu toEntity(MenuGroup menuGroup) {
        List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Menu(name, price, menuGroup, menuProducts);
    }
}
