package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuProductTestFixture.짜장면메뉴상품;
import static kitchenpos.fixture.MenuProductTestFixture.짬뽕메뉴상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest {

    @Test
    void of() {
        // given
        MenuProduct 짜장면메뉴상품 = 짜장면메뉴상품();
        MenuProduct 짬뽕메뉴상품 = 짬뽕메뉴상품();
        List<MenuProduct> 메뉴상품리스트 = Arrays.asList(짜장면메뉴상품, 짬뽕메뉴상품);

        // when
        MenuProducts menuProducts = MenuProducts.from(메뉴상품리스트);

        // then
        assertThat(menuProducts.value()).isEqualTo(메뉴상품리스트);
    }

    @Test
    void ofWithException1() {
        // given
        List<MenuProduct> 메뉴상품리스트 = new ArrayList();

        // when & then
        assertThatThrownBy(() -> MenuProducts.from(메뉴상품리스트))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void totalPrice() {
        // given
        MenuProduct 짜장면메뉴상품 = 짜장면메뉴상품();
        MenuProduct 짬뽕메뉴상품 = 짬뽕메뉴상품();
        Price expectedPrice = 짜장면메뉴상품.totalPrice().add(짬뽕메뉴상품.totalPrice());
        List<MenuProduct> 메뉴상품리스트 = Arrays.asList(짜장면메뉴상품, 짬뽕메뉴상품);
        MenuProducts menuProducts = MenuProducts.from(메뉴상품리스트);

        // when & then
        assertThat(menuProducts.totalPrice()).isEqualTo(expectedPrice);
    }
}
