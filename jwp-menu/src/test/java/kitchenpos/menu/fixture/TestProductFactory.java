package kitchenpos.menu.fixture;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

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
}
