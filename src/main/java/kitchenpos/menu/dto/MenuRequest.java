package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getMenuProductIds() {
        return menuProducts.stream().map(MenuProductRequest::getProductId).collect(toList());
    }

    public Menu toMenu(final MenuGroup menuGroup, final List<Product> products) {
        Menu menu = Menu.builder()
                .name(name)
                .price(price)
                .menuGroup(menuGroup)
                .build();
        List<MenuProduct> newMenuProducts = menuProducts.stream()
                .filter(request -> products.stream().anyMatch(product -> product.getId().equals(request.getProductId())))
                .map(request -> request.toMenuProducts(menu, products))
                .collect(toList());
        menu.addMenuProducts(newMenuProducts);
        return menu;
    }
}


