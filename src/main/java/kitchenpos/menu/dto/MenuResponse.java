package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProducts;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MenuResponse() {
    }

    private MenuResponse(Long id) {
        this.id = id;
    }

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = MenuGroupResponse.of(menuGroup);
        this.menuProducts = menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getAmount(), menu.getMenuGroup(), menu.getMenuProducts(),menu.getCreatedDate(), menu.getModifiedDate());
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

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public void setMenuGroupResponse(MenuGroupResponse menuGroupResponse) {
        this.menuGroupResponse = menuGroupResponse;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
