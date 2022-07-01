package kitchenpos.product.domain;

import java.util.List;

public class Products {
    private final List<Product> products;
    public Products(List<Product> products) {
        this.products = products;
    }

    public Product getProduct(Long productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("[ERROR] 상품 변환에 오류가 발생하였습니다."));
    }
}
