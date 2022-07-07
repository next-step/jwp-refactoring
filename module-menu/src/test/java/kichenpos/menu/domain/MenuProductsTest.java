package kichenpos.menu.domain;

import kichenpos.common.domain.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest {

    @Test
    void 메뉴의_가격은_메뉴_상품들_가격의_총합보다_높을_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                new MenuProducts(createMenuProducts(), new Price(BigDecimal.valueOf(15000)), null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
    }

    public static List<MenuProduct> createMenuProducts() {
        MenuProduct menuProduct = new MenuProduct(new Product("치킨", new Price(BigDecimal.valueOf(5000))), 2);
        MenuProduct menuProduct1 = new MenuProduct(new Product("떡볶이", new Price(BigDecimal.valueOf(2000))), 2);

        return Arrays.asList(menuProduct, menuProduct1);
    }
}
