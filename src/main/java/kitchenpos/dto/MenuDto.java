package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Money;

public class MenuDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProductDto> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuDto() {
    }

    public MenuDto(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        List<MenuProduct> menuProductDtoList = menuProducts.stream()
                .map(MenuProductDto::toEntity)
                .collect(Collectors.toList());
        return new Menu(id, name, Money.won(price.longValue()), menuGroupId, menuProductDtoList);
    }

    public static MenuDto of(Menu menu) {
        List<MenuProductDto> menuProducts = menu.getMenuProduct().stream()
                .map(it -> MenuProductDto.of(it, menu.getId()))
                .collect(Collectors.toList());
        BigDecimal price = BigDecimal.valueOf(menu.getPrice().amount);
        return new MenuDto(menu.getId(), menu.getName(), price, menu.getMenuGroupId(), menuProducts);
    }
}
