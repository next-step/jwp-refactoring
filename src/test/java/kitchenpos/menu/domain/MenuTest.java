package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuGroup 추천_메뉴;
    private Product 후라이드_치킨;
    private MenuProduct 메뉴_후라이드_치킨;

    @BeforeEach
    void init() {
        // given
        추천_메뉴 = new MenuGroup(1L, "추천메뉴");
        후라이드_치킨 = new Product(1L, "후라이드치킨", new BigDecimal(8_500L));
    }

    @Test
    @DisplayName("메뉴 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // when then
        assertAll(
            () -> assertThatThrownBy(() -> new Menu("후라이드+후라이드", new BigDecimal(-6_000L), 추천_메뉴))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> new Menu("후라이드+후라이드", null, 추천_메뉴))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품 금액의 합보다 클 경우 - 오류")
    void inCaseOfMenuProductsPriceThanMenuPrice() {
        // given
        Menu menu = new Menu("후라이드+후라이드", new BigDecimal(30_000L), 추천_메뉴);

        // when then
        assertThatThrownBy(() -> menu.addMenuProduct(후라이드_치킨, 2))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
