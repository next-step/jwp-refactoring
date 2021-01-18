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

    public Set<Long> getProductIds() {
        return this.menuProductRequests.stream()
              .map(MenuProductRequest::getProductId)
              .collect(Collectors.toSet());
    }

    public List<MenuProduct> toMenuProducts(MenuGroup menuGroup, List<Product> products) {
        Menu menu = toMenuEntity(menuGroup);

        Map<Long, Product> productInfo = products.stream()
              .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<MenuProduct> menuProducts = menuProductRequests.stream()
              .filter(menuProductRequest -> productInfo
                    .containsKey(menuProductRequest.getProductId()))
              .map(menuProductRequest -> {
                  Product product = productInfo.get(menuProductRequest.getProductId());
                  return new MenuProduct(menu, product, menuProductRequest.getQuantity());
              })
              .collect(Collectors.toList());

        validatePrice(sumOfMenuProductPrice(menuProducts));
        return menuProducts;
    }

    private BigDecimal sumOfMenuProductPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
              .map(MenuProduct::sumOfPrice)
              .reduce((p1, p2) -> p1.add(p2))
              .orElseThrow(IllegalArgumentException::new);
    }

    private void validatePrice(BigDecimal sumOfMenuProductPrice) {
        if (price.compareTo(sumOfMenuProductPrice) != 0) {
            throw new IllegalArgumentException("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
        }
    }

    private Menu toMenuEntity(MenuGroup menuGroup) {
        return new Menu(this.name, this.price, menuGroup);
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
}
