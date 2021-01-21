package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId) {
        this(name, price, menuGroupId, Collections.emptyList());
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public BigDecimal getSumPrice(List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = products.stream()
                    .filter(filterProduct -> menuProductRequest.getProductId() == filterProduct.getId())
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getSumPrice(menuProductRequest.getQuantity()));
        }

        return sum;
    }

    public List<MenuProduct> createMenuProducts(List<Product> products) {
        return menuProducts.stream().map(menuProductRequest -> {
            Product product = products.stream()
                    .filter(filterProduct -> menuProductRequest.getProductId() == filterProduct.getId())
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            return new MenuProduct(product, menuProductRequest.getQuantity());
        }).collect(Collectors.toList());
    }
}
