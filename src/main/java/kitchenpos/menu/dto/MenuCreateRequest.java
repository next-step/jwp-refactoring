package kitchenpos.menu.dto;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    protected MenuCreateRequest() {}

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts.addAll(menuProducts);
    }

    public Menu of(MenuGroup menuGroup, Products products) {
        return new Menu(name, new Price(price), menuGroup, convertMenuProductsByRequest(products));
    }

    private MenuProducts convertMenuProductsByRequest(final Products products) {
        checkAllProductsIsExist(products);

        List<MenuProduct> menuProducts = this.getMenuProducts()
                .stream()
                .map(request -> {
                    Product product = products.findMenuById(request.getProductId());
                    return new MenuProduct(product, new Quantity(request.getQuantity()));
                })
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
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
        return this.menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private void checkAllProductsIsExist(final Products products) {
        if (products.isNotAllContainIds(getProductIds())) {
            throw new IllegalArgumentException("메뉴에 저장되지 않은 상품이 존재합니다.");
        }
    }
}
