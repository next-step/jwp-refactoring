package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(List<MenuProductRequest> menuProducts) {
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
        if (menuProducts == null) {
            return Collections.emptyList();
        }

        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    public List<MenuProduct> toMenuProducts(List<Product> productList) {
        return productList.stream()
            .map(this::makeMenuProduct)
            .collect(Collectors.toList());
    }

    private MenuProduct makeMenuProduct(Product product) {
        return menuProducts.stream()
            .filter(menuProductRequest -> menuProductRequest.isEqualProductId(product.getId()))
            .map(menuProductRequest -> new MenuProduct(product, menuProductRequest.getQuantity()))
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }
}
