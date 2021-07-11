package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.presentation.dto.exception.BadProductIdException;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    private MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(null, name, price, menuGroupId, menuProductRequests);
    }

    public List<Long> getProductsIds() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProductsBy(List<Product> products) {
        return products.stream()
                .map(this::createMenuProductWith)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProductWith(Product product) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> isProductIdMatch(menuProductRequest, product))
                .map(menuProductRequest -> MenuProduct.of(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadProductIdException::new);
    }

    private boolean isProductIdMatch(MenuProductRequest menuProductRequest, Product product) {
        return Objects.equals(menuProductRequest.getProductId(), product.getId());
    }

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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
