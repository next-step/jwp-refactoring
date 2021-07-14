package kitchenpos.product.domain;


import kitchenpos.menu.dto.MenuProductRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Products {
    private static final String NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE = "미등록 상품을 메뉴 상품으로 등록 요청 하였습니다.";

    private List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public boolean contains(Product product) {
        return this.products.contains(product);
    }

    public BigDecimal calcProductsPrice(List<MenuProductRequest> menuProductRequests) {
        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        return menuProductRequests
                .stream()
                .filter(menuProductRequest -> productMap.containsKey(menuProductRequest.getProductId()))
                .map(menuProductRequest -> productMap.get(menuProductRequest.getProductId()).multiplyQuantity(menuProductRequest.getQuantity()))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void checkProductsSize(int size) {
        if (products.size() != size) {
            throw new IllegalArgumentException(NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Products products1 = (Products) object;
        return Objects.equals(products, products1.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
