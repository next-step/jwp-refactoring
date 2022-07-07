package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
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
        메뉴_후라이드_치킨 = new MenuProduct(후라이드_치킨, 2);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품 금액의 합보다 클 경우 - 오류")
    void inCaseOfMenuProductsPriceThanMenuPrice() {
        // when then
        assertThatThrownBy(() -> new Menu("후라이드+후라이드", new BigDecimal(30_000L), 추천_메뉴, Arrays.asList(메뉴_후라이드_치킨)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
