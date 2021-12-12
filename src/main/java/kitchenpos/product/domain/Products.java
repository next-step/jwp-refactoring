package kitchenpos.product.domain;

import java.util.Collections;
import java.util.List;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

public final class Products {

    private final List<Product> products;

    private Products(List<Product> products) {
        Assert.notNull(products, "상품 리스트는 필수입니다.");
        Assert.noNullElements(products,
            () -> String.format("상품 리스트(%s)에 null이 포함될 수 없습니다.", products));
        this.products = products;
    }

    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public static Products singleton(Product product) {
        return new Products(Collections.singletonList(product));
    }

    public Price sumPrice() {
        return products.stream()
            .map(Product::price)
            .reduce(Price::sum)
            .orElse(Price.ZERO);
    }
}
