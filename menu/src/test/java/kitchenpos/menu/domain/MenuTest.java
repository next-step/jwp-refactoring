package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doThrow;

public class MenuTest {
    private MenuValidator menuValidator;

    @BeforeEach
    void setUp() {
        menuValidator = Mockito.mock(MenuValidator.class);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void testCreate() {
        // given
        Long menuGroupId = 1L;

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1));
        menuProducts.add(new MenuProduct(2L, 1));
        Menu expectedMenu = new Menu(1L, Name.of("집밥이최고"), Price.of(16000), menuGroupId, new MenuProducts(menuProducts));

        // when
        Menu menu = Menu.create(expectedMenu.getName(), expectedMenu.getPrice(), menuGroupId, new MenuProducts(menuProducts), menuValidator);

        // then
        assertAll(
                () -> assertThat(menu.getName()).isEqualTo(expectedMenu.getName()),
                () -> assertThat(menu.getPrice()).isEqualTo(expectedMenu.getPrice()),
                () -> assertThat(menu.getMenuProducts()).isEqualTo(expectedMenu.getMenuProducts())
        );
    }

    @DisplayName("메뉴의 가격은 0원 보다 커야 한다")
    @Test
    void givenZeroPriceThenThrowException() {
        // given
        String name = "대표 메뉴";
        long price = 0;
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Menu.create(name, price, menuGroupId, new MenuProducts(menuProducts), menuValidator);

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
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Menu.create(name, price, menuGroupId, new MenuProducts(menuProducts), menuValidator);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 포함된 상품 가격의 합보다 작거나 같아야 한다")
    @Test
    void givenLessThanProductSumPriceThenThrowException() {
        // given
        int menuPrice = 17000;
        Long menuGroupId = 1L;

        List<MenuProduct> menuProductList = new ArrayList<>();
        menuProductList.add(new MenuProduct(1L, 1));
        menuProductList.add(new MenuProduct(2L, 1));
        MenuProducts menuProducts = new MenuProducts(menuProductList);

        doThrow(IllegalArgumentException.class).when(menuValidator).validateMenuPrice(menuPrice, menuProducts.getProductIds());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> Menu.create("집밥이최고", menuPrice, menuGroupId, menuProducts, menuValidator);

        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
