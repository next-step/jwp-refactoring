package kitchenpos.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuTest {
    @DisplayName("메뉴를 생성한다")
    @Test
    void testCreate() {
        // given
        String name = "대표 메뉴";
        long price = 16000;
        MenuGroup menuGroup = new MenuGroup();
        List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        Menu menu = new Menu(name, price, menuGroup, menuProducts);

        // then
        assertAll(
                () -> assertThat(menu.getName()).isEqualTo(name)
        );
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
        ThrowableAssert.ThrowingCallable callable = () -> new Menu(name, price, menuGroup, menuProducts);

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
        ThrowableAssert.ThrowingCallable callable = () -> new Menu(name, price, menuGroup, menuProducts);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
