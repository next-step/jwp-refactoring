package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId,
          List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public Set<Long> getProductIds() {
        return this.menuProductRequests.stream()
              .map(MenuProductRequest::getProductId)
              .collect(Collectors.toSet());
    }

    public Menu toEntity(MenuGroup menuGroup, List<Product> products) {
        return new Menu(this.name, this.price, menuGroup, toMenuGroupEntities(products));
    }

    private List<MenuProduct> toMenuGroupEntities(List<Product> products) {
        Map<Long, Product> productInfo = products.stream()
              .collect(Collectors.toMap(Product::getId, Function.identity()));

        return menuProductRequests.stream()
              .filter(menuProductRequest -> productInfo.containsKey(menuProductRequest.getProductId()))
              .map(menuProductRequest -> {
                  Product product = productInfo.get(menuProductRequest.getProductId());
                  return new MenuProduct(product, menuProductRequest.getQuantity());
              })
              .collect(Collectors.toList());
    }
}
