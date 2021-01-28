package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
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

    public List<Long> getProductsId() {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    public List<MenuProduct> toEntity(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(name, price, menuGroup);
        List<MenuProduct> entityMenuProducts = menuProducts.stream()
            .map(menuProduct -> new MenuProduct(menu, getMatchProductInProducts(products, menuProduct.getProductId()), menuProduct.getQuantity()))
            .collect(Collectors.toList());

        validate(entityMenuProducts);
        return entityMenuProducts;
    }

    private void validate(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getAmount());
        }
        if (!price.equals(sum)) {
            throw new IllegalArgumentException("메뉴의 금액은 메뉴 상품의 합과 같아야 한다.");
        }
    }

    private Product getMatchProductInProducts(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
