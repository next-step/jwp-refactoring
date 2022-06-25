package kitchenpos.domain;

import static kitchenpos.fixture.MenuGroupFixture.메뉴묶음_데이터_생성;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품_데이터_생성;
import static kitchenpos.fixture.ProductFixture.상품_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("가격이 상품가격의 합보다 크면 생성할 수 없다.")
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