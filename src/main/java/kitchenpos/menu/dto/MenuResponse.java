package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuResponse {
    private Long id;
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu) {
        id = menu.getId();
        name = menu.getName();
        price = menu.getPrice().getValue();
        menuGroupId = menu.getMenuGroupId();
        menuProducts = menu.getMenuProducts().getMenuProducts().stream()
                .map(MenuProductResponse::new)
                .collect(toList());
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProductResponse> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
