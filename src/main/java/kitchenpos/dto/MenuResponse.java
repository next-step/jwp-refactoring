package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
                        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu){
        MenuProducts menuProducts = menu.getMenuProducts();
        List<MenuProductResponse> menuProductResponses = menuProducts.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getPrice(), menu.getMenuGroup().getId(), menuProductResponses);
    }

}
