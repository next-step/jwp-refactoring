package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.메뉴묶음_데이터_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품_데이터_생성;
import static kitchenpos.fixture.ProductFixture.상품_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {

    @Test
    void checkValidPrice_fail_menuPriceGe() {
        //given
        MenuGroup menuGroup = 메뉴묶음_데이터_생성(1L, "name");
        Menu menu = new Menu("name", BigDecimal.valueOf(1001), menuGroup);

        Product product1 = 상품_데이터_생성(1L, "name", BigDecimal.valueOf(300));
        MenuProduct menuProduct1 = 메뉴상품_데이터_생성(1L, product1, 3);
        Product product2 = 상품_데이터_생성(2L, "name", BigDecimal.valueOf(100));
        MenuProduct menuProduct2 = 메뉴상품_데이터_생성(1L, product2, 1);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct1, menuProduct2);

        //when//then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menu.addMenuProducts(menuProducts));
    }
}