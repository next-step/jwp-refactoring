package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest() {
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
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

    public void validatePrice() {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 필수이고, 0원 이상이어야합니다.");
        }
    }

    private BigDecimal sumOfPriceForProducts(List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = products.stream()
                    .filter(filterProduct -> menuProductRequest.getProductId().equals(filterProduct.getId()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getSumOfProducts(menuProductRequest.getQuantity()));
        }
        return sum;
    }

    public void validateSumForProducts(List<Product> products) {
        BigDecimal sum = sumOfPriceForProducts(products);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> createMenuProducts(List<Product> products) {
        List<MenuProduct> createMenuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = products.stream()
                    .filter(p -> menuProductRequest.getProductId().equals(p.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("메뉴로 등록하려는 상품이 존재하지 않습니다."));
            createMenuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return createMenuProducts;
    }
}
