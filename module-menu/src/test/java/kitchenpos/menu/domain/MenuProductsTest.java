package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 목록에 대한 단위 테스트")
class MenuProductsTest {

    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = mock(Menu.class);
    }

    @DisplayName("메뉴 상품목록에 메뉴를 매핑하면 정상적으로 매핑된다")
    @Test
    void create_test() {
        // given
        MenuProduct menuProduct = MenuProduct.of(1L, 3);
        MenuProduct menuProduct2 = MenuProduct.of(2L, 3);
        when(menu.getId()).thenReturn(1L);

        // given
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct, menuProduct2), menu);

        // then
        assertNotNull(menuProducts.getItems().get(0).getMenu());
        assertNotNull(menuProducts.getItems().get(1).getMenu());
    }
}
