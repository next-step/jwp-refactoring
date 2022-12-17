package kitchenpos.dto;

import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    private List<MenuProductRequest> menuProduct;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProduct) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProduct = menuProduct;
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

    public List<MenuProductRequest> getMenuProduct() {
        return menuProduct;
    }

    public List<Long> getProductId() {
        return menuProduct.stream().map(MenuProductRequest::getProductId).collect(Collectors.toList());
    }

    public MenuProducts makeMenuProducts(List<Product> product) {
        return new MenuProducts(menuProduct.stream()
                .map(menuProduct -> menuProduct.makeMenuProduct(product))
                .collect(Collectors.toList()));
    }
}
