package kitchenpos.menu.domain;

import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("메뉴 상품 목록 테스트")
class MenuProductsTest {
    private MenuProduct 강정치킨_메뉴상품;
    private MenuProduct 후라이드치킨_메뉴상품;

    @BeforeEach
    void setup() {
        Product 강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        Product 후라이드치킨 = ProductFixture.create(2L, "후라이드치킨", BigDecimal.valueOf(16_000));

        강정치킨_메뉴상품 = MenuProductFixture.create(강정치킨.getId(), 1L);
        후라이드치킨_메뉴상품 = MenuProductFixture.create(후라이드치킨.getId(), 1L);
    }

    @DisplayName("메뉴 상품 목록 생성 확인")
    @Test
    void 메뉴_상품_목록_생성_확인() {
        // when
        ThrowableAssert.ThrowingCallable 생성_요청 = () -> MenuProducts.of(Arrays.asList(강정치킨_메뉴상품, 후라이드치킨_메뉴상품));

        // then
        assertThatNoException().isThrownBy(생성_요청);
    }
}
