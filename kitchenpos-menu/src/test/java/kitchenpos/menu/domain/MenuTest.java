package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MenuTest {
    private Product 후라이드치킨;
    private Product 양념치킨;
    private Product 콜라;
    private MenuGroup 후라이드치킨세트;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "후라이드치킨", 15_000L);
        콜라 = Product.of(2L, "콜라", 2_000L);
        양념치킨 = Product.of(3L, "양념치킨", 16_000L);
        후라이드치킨세트 = MenuGroup.of(1L, "후라이드치킨세트");
        후라이드치킨상품 = MenuProduct.of(후라이드치킨, 1L);
        콜라상품 = MenuProduct.of(콜라, 1L);
        양념치킨상품 = MenuProduct.of(양념치킨, 1L);
    }

    @Test
    void 메뉴_생성() {
        // given
        String name = "양념치킨세트";
        Long price = 8500L;

        // when
        Menu 양념치킨세트 = Menu.of(1L, name, BigDecimal.valueOf(price), 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품));

        // then
        assertAll(
                () -> assertThat(양념치킨세트.getName().value()).isEqualTo(name),
                () -> assertThat(양념치킨세트.getMenuProducts()).containsExactly(후라이드치킨상품, 양념치킨상품, 콜라상품)
        );
    }

    @Test
    void 메뉴그룹_없는_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        Long price = 8_500L;

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, BigDecimal.valueOf(price), null, Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.REQUIRED_MENU.message());
    }

    @Test
    void 메뉴상품_가격_합보다_큰_가격_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        Long price = 100_000L;

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, BigDecimal.valueOf(price), 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.MENU_PRICE_OVER_TOTAL_PRICE.message());
    }

    @Test
    void 가격_없는_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        BigDecimal price = null;

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, price, 후라이드치킨세트.getId(),
                Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.PRICE_IS_NOT_NULL.message());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, -2030})
    void 음수_가격_메뉴_생성(Long price) {
        // given
        String name = "양념치킨세트";

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, BigDecimal.valueOf(price), 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.PRICE_UNDER_ZERO.message());
    }
}
