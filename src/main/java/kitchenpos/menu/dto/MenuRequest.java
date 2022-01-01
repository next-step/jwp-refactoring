package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static <T> MenuRequest of(String name, int price, MenuGroup menuGroup, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroup.getId(), menuProductRequests);
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public List<Long> getProductIds() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public Long getQuantity(Long productId) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> menuProductRequest.getProductId().equals(productId))
                .map(menuProductRequest -> menuProductRequest.getQuantity())
                .findFirst()
                .get();
    }

    public MenuProduct createMenuProduct(Product product) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> menuProductRequest.getProductId().equals(product.getId()))
                .map(menuProductRequest -> menuProductRequest.toEntity(product))
                .findFirst()
                .get();
    }
}
