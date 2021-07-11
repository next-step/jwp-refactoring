package kitchenposNew.menu.dto;

import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuProduct;
import kitchenposNew.menu.domain.MenuProducts;
import kitchenposNew.menu.domain.Product;
import kitchenposNew.wrap.Price;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    public String name;
    public BigDecimal price;
    public Long menuGroupId;
    public List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public Menu toMenu(List<Product> products) {
        List<MenuProduct> menuProducts = toMenuProduct(products);
        return new Menu(name, new Price(price), menuGroupId, new MenuProducts(menuProducts));
    }

    private List<MenuProduct> toMenuProduct(List<Product> products) {
        return products.stream()
                .map(this::findMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct findMenuProduct(Product product) {
        return menuProductRequests.stream()
                .filter(menuProductRequest -> menuProductRequest.getProductId() == product.getId())
                .map(menuProductRequest -> new MenuProduct(product, menuProductRequest.getQuantity()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public List<Long> getProductIds() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }
}
