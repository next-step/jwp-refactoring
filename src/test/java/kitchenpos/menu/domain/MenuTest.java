package kitchenpos.menu.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {
    @DisplayName("메뉴를 생성한다")
    @Test
    void testCreate() {
        // given
        Product 볶음짜장면 = new Product(1L, "볶음짜장면", 8000);
        Product 삼선짬뽕 = new Product(2L, "삼선짬뽕", 8000);

        MenuGroup menuGroup = new MenuGroup(1L, "식사류");

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(볶음짜장면, 1));
        menuProducts.add(new MenuProduct(삼선짬뽕, 1));
        Menu expectedMenu = Menu.create(1L, "집밥이최고", 16000, new MenuGroup(), new MenuProducts(menuProducts));

        // when
        Menu menu = Menu.create(expectedMenu.getName(), expectedMenu.getPrice(), menuGroup, new MenuProducts(menuProducts));

        // then
        assertThat(menu).isEqualTo(menu);
    }

    @DisplayName("메뉴의 가격은 0원 보다 커야 한다")
    @Test
    void givenZeroPriceThenThrowException() {
        // given
        String name = "대표 메뉴";
        long price = 0;
        MenuGroup menuGroup = new MenuGroup();
        List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Menu.create(name, price, menuGroup, new MenuProducts(menuProducts));

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 이름을 입력해야 한다")
    @Test
    void givenEmptyNameThenThrowException() {
        // given
        String name = "";
        long price = 16000;
        MenuGroup menuGroup = new MenuGroup();
        List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Menu.create(name, price, menuGroup, new MenuProducts(menuProducts));

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
