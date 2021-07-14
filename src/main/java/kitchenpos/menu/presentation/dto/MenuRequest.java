package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.presentation.dto.exception.BadProductIdException;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public List<Long> getProductsIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProductsBy(List<Product> products) {
        return products.stream()
                .map(this::createMenuProductWith)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProductWith(Product product) {
        return menuProducts.stream()
                .filter(menuProductRequest -> isProductIdMatch(menuProductRequest, product))
                .map(menuProductRequest -> MenuProduct.of(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(BadProductIdException::new);
    }

    private boolean isProductIdMatch(MenuProductRequest menuProductRequest, Product product) {
        return Objects.equals(menuProductRequest.getProductId(), product.getId());
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
}
