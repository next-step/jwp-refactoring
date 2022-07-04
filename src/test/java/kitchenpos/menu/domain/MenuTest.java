package kitchenpos.menu.domain;

import kitchenpos.product.domain.ProductTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {
    public static Menu 햄버거메뉴 = new Menu(1L, "불고기버거", BigDecimal.valueOf(1500),
            MenuGroupTest.햄버거_메뉴, Arrays.asList(MenuProduct.of(null, ProductTest.불고기버거.getId(), 5)));

    @Test
    @DisplayName("메뉴 생성")
    void create() {
        // then
        assertThat(햄버거메뉴).isInstanceOf(Menu.class);
    }
}
