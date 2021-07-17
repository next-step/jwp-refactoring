package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class MenuRequest {
    private static final String INVALID_PRODUCT = "잘못된 상품 정보";
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }

    public void registerMenu(Menu menu) {
        for (MenuProductRequest request : menuProducts) {
            MenuProduct menuProduct = new MenuProduct(request.getProductId(), request.getQuantity());
            menu.registerMenuProduct(menuProduct);
        }
    }

}
