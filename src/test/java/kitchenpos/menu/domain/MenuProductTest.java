package kitchenpos.menu.domain;

import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
    }
    @Test
    void 메뉴_상품_생성() {
        MenuProduct actual = MenuProduct.of(후라이드치킨, 5);

        assertThat(actual).isNotNull();
    }

    @Test
    void 메뉴_상품의_수량은_0개_이상이다() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> MenuProduct.of(후라이드치킨, -1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴 상품의 수량는 0개 이상 이어야 합니다.");
    }
}
