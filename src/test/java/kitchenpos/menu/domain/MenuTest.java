package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuTest {
    private Product 후라이드치킨;
    private Product 양념치킨;
    private Product 콜라;
    private MenuGroup 후라이드치킨세트;
    private MenuProduct 후라이드치킨상품;
    private MenuProduct 양념치킨상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(15000));
        콜라 = Product.of(2L, "콜라", BigDecimal.valueOf(2000));
        양념치킨 = Product.of(3L, "양념치킨", BigDecimal.valueOf(16000));
        후라이드치킨세트 = MenuGroup.of(1L, "후라이드치킨세트");
        후라이드치킨상품 = MenuProduct.of(1L, null, 후라이드치킨, 1L);
        콜라상품 = MenuProduct.of(2L, null, 콜라, 1L);
        양념치킨상품 = MenuProduct.of(3L, null, 양념치킨, 1L);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        // given
        String name = "양념치킨세트";
        BigDecimal price = BigDecimal.valueOf(8500);

        // when
        Menu 양념치킨세트 = Menu.of(1L, name, price, 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품));

        // then
        assertAll(
                () -> assertThat(양념치킨세트.getName()).isEqualTo(name),
                () -> assertThat(양념치킨세트.getMenuProducts()).containsExactly(후라이드치킨상품, 양념치킨상품, 콜라상품)
        );
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴그룹_없는_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        BigDecimal price = BigDecimal.valueOf(8500);

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, price, null, Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성할 수 없다.")
    @Test
    void 메뉴상품_가격_합보다_큰_가격_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        BigDecimal price = BigDecimal.valueOf(100000);

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, price, 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 시, 가격이 비어있으면 에러가 발생한다.")
    @Test
    void 가격_없는_메뉴_생성() {
        // given
        String name = "양념치킨세트";
        BigDecimal price = null;

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, price, 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "메뉴 생성 시, 가격이 음수이면 에러가 발생한다. (가격: {0})")
    @ValueSource(longs = {-1000, -2030})
    void 음수_가격_메뉴_생성(long price) {
        // given
        String name = "양념치킨세트";

        // when & then
        assertThatThrownBy(() -> Menu.of(1L, name, BigDecimal.valueOf(price), 후라이드치킨세트.getId(), Arrays.asList(후라이드치킨상품, 양념치킨상품, 콜라상품)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
