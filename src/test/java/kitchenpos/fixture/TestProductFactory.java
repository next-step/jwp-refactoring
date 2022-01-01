package kitchenpos.fixture;

import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestProductFactory {
    public static Product 상품_조회됨(final Long id, final String name, final long price) {
        return Product.of(id, name, price);
    }

    public static Product 상품_생성됨(final Long id, final String name, final long price) {
        return Product.of(id, name, price);
    }

    public static ProductRequest 상품_요청(final String name, final long price) {
        return ProductRequest.of(name, price);
    }

    public static ProductResponse 상품_응답(final Long id, final String name, final long price) {
        return ProductResponse.of(id, name, price);
    }

    public static void 상품_생성됨(ProductResponse actual, Product product) {
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(product.getId()),
                () -> assertThat(actual.getPrice()).isEqualTo(product.getPrice().toLong()),
                () -> assertThat(actual.getName()).isEqualTo(product.getName())
        );
    }

    public static List<Product> 상품_목록_조회됨(int countProduct) {
        final List<Product> products = new ArrayList<>();
        for (int i = 1; i <= countProduct; i++) {
            products.add(Product.of(Long.valueOf(i), "상품", 5000));
        }
        return products;
    }

    public static void 상품_목록_확인됨(List<ProductResponse> actual, List<Product> products) {
        assertThat(actual).hasSize(products.size());
    }
}
