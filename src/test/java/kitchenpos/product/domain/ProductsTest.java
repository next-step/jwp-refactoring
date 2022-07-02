package kitchenpos.product.domain;

import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsTest {
    Product product1;
    Product product2;
    Products products;

    @BeforeEach
    void setUp() {
        product1 = TestProductFactory.create(1L, "진라면", 100);
        product2 = TestProductFactory.create(2L, "진라면", 100);
        products = new Products(Arrays.asList(product1, product2));
    }

    @Test
    @DisplayName("상품 개수가 같으면 true 를 반환한다")
    void isSameSizeTrue() {
        // given
        List<Long> productIds = Arrays.asList(product1.getId(), product2.getId(), product2.getId(), product2.getId());

        // when
        boolean sameSize = products.isSameSize(productIds);

        // then
        assertThat(sameSize).isTrue();
    }

    @Test
    @DisplayName("상품 개수가 다르면 false 를 반환한다")
    void isSameSizeFalse() {
        // given
        List<Long> productIds = Collections.singletonList(product1.getId());

        // when
        boolean sameSize = products.isSameSize(productIds);

        // then
        assertThat(sameSize).isFalse();
    }
}
