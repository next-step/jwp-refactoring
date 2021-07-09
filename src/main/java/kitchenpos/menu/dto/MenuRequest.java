package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

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

    public List<MenuProduct> getMenuProductsBy(List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException(INVALID_PRODUCT);
        }
        return products.stream()
                .map(this::changeToMenuProduct)
                .collect(toList());
    }

    private MenuProduct changeToMenuProduct(Product product) {
        return menuProducts.stream()
                .filter(v -> isProductIdMatch(product, v))
                .map(v -> new MenuProduct(product, v.getQuantity()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_PRODUCT));
    }

    private boolean isProductIdMatch(Product product, MenuProductRequest v) {
        return v.getProductId().equals(product.getId());
    }
}
