package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.model.Menu;
import kitchenpos.domain.model.MenuProduct;
import kitchenpos.domain.model.Money;

public class MenuDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

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

    public static MenuDto of(Menu menu) {
        List<MenuProductDto> menuProducts = menu.getMenuProduct().stream()
                .map(it -> MenuProductDto.of(it, menu.getId()))
                .collect(Collectors.toList());
        BigDecimal price = menu.getPrice().amount;
        return new MenuDto(menu.getId(), menu.getName(), price, menu.getMenuGroupId(), menuProducts);
    }
}
