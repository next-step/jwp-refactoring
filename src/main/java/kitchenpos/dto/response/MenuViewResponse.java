package kitchenpos.dto.response;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuViewResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductViewResponse> menuProducts;

    public static MenuViewResponse of(Menu menu) {
        List<MenuProductViewResponse> productViewResponses = menu.getMenuProducts()
                .stream()
                .map(MenuProductViewResponse::of)
                .collect(Collectors.toList());

        return new MenuViewResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getPrice(),
                menu.getMenuGroup().getId(),
                productViewResponses
        );
    }

    public MenuViewResponse(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductViewResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductViewResponse> getMenuProducts() {
        return menuProducts;
    }
}
