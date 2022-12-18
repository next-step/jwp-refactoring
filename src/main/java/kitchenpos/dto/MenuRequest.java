package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    private List<MenuProductRequest> menuProduct;

    protected MenuRequest() {
    }

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

    public MenuProducts makeMenuProducts(List<Product> product) {
        return new MenuProducts(menuProduct.stream()
                .map(menuProduct -> menuProduct.makeMenuProduct(product))
                .collect(Collectors.toList()));
    }
}
