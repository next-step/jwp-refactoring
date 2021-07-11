package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    @DisplayName("메뉴를 생성하고 생성자의 값을 리턴한다.")
    @Test
    void create() {
        Product product = new Product("후라이드치킨", BigDecimal.valueOf(16000));
        MenuProduct menuProduct = new MenuProduct(product, 1L);
        Menu menu = Menu.createWithMenuProduct("추천메뉴", BigDecimal.valueOf(16000), new MenuProducts(Arrays.asList(menuProduct)), null);

        assertThat(menu).isNotNull();
    }
}
