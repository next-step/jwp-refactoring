package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void create() {
        // given
        Menu mockMenu = mock(Menu.class);
        Product product1 = new Product("name", BigDecimal.ONE);
        Product product2 = new Product("name", BigDecimal.ONE);
        final MenuProduct menuProduct1 = new MenuProduct(mockMenu, product1, 1L);
        final MenuProduct menuProduct2 = new MenuProduct(mockMenu, product2, 2L);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);
        final BigDecimal price = new BigDecimal(1000);
        given(mockMenu.getId()).willReturn(1L);

        // when
        final Throwable throwable = catchThrowable(() -> new Menu("name", new Price(price), new MenuGroup(), new MenuProducts(menuProducts)));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
