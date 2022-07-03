package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.domain.MenuProductsTest.createMenuProducts;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴의_가격은_메뉴_상품_가격의_총합보다_높을_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                new Menu("황금치킨", new Price(BigDecimal.valueOf(15000)), 1L, createMenuProducts())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
    }
}
