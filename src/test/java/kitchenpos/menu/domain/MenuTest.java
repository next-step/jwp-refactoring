package kitchenpos.menu.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

@DisplayName("메뉴 테스트")
class MenuTest {

    @DisplayName("메뉴 생성 시 가격은 음수가 나올 수 없다.")
    @Test
    void menu_price_not_negative_number() {
        // given && when && then
        Assertions.assertThatThrownBy(() -> new Menu("name", BigDecimal.valueOf(-1000), null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 합보다 크다면 등록할 수 없다")
    @Test
    void menu_price_less_then_menu_product_sum() {
        // given
        MenuProduct 후라이드치킨 = new MenuProduct(new Product("후라이드치킨", BigDecimal.valueOf(15_000)), 1);
        MenuProduct 양념치킨 = new MenuProduct(new Product("양념치킨", BigDecimal.valueOf(20_000)), 2);

        // when && then
        Assertions.assertThatThrownBy(() -> new Menu("menu", BigDecimal.valueOf(56_000), null, Arrays.asList(후라이드치킨, 양념치킨)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
