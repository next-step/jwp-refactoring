package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void create() {
        // given
        final MenuProduct menuProduct1 = new MenuProduct(1L, 1L, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(2L, 2L, 2L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final Product product1 = new Product(1L, "name", new BigDecimal(100));
        final Product product2 = new Product(2L, "name", new BigDecimal(200));
        List<Product> productList = Arrays.asList(product1, product2);
        final Products products = new Products(productList);
        final BigDecimal price = new BigDecimal(1000);

        // when
        final Throwable throwable = catchThrowable(() -> new Menu("name", new Price(price), 1L, menuProducts, products));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
