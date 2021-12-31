package kitchenpos.infra.product;

import kitchenpos.core.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductTest {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @DisplayName("상품은 아이디, 이름, 가격으로 구성되어 있다.")
    @Test
    void create() {
        // given
        Product menuProduct = Product.of("쌀국수", 13_000);
        // when
        final Product actual = jpaProductRepository.save(menuProduct);
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertTrue(actual.matchName("쌀국수")),
                () -> assertTrue(actual.matchPrice(13_000))
        );
    }
}
