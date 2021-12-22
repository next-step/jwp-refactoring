package kitchenpos.menu.domain;

import kitchenpos.product.infra.ProductRepository;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품은 아이디, 이름, 가격으로 구성되어 있다.")
    @Test
    void create() {
        // given
        Product menuProduct = Product.of("쌀국수", 13_000);
        // when
        final Product actual = productRepository.save(menuProduct);
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertTrue(actual.matchName("쌀국수")),
                () -> assertTrue(actual.matchPrice(13_000))
        );
    }

    @DisplayName("가격은 0보다 작을 경우 에러가 발생한다.")
    @Test
    void price() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of("쌀국수", -1);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 빈 값일 수 없다.")
    @Test
    void priceEmpty() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of("쌀국수", null);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "이름은 빈값이 될 수 없다. \"{0}\"")
    @NullAndEmptySource
    void nameEmpty(String name) {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of(name, null);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}