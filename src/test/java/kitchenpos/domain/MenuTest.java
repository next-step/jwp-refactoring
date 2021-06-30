package kitchenpos.domain;

import kitchenpos.exception.MenuCheapException;
import kitchenpos.exception.ProductNotExistException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    @DisplayName("메듀에 등록할 메뉴와 메뉴 수가 틀리면 ProductNotExistException이 발생한다 ")
    void 메뉴에_등록할_메뉴와_메뉴_수가_틀리면_ProductNotExistException이_발생한다() {
        // given
        List<MenuProductCreate> menuProductCreates = Arrays.asList(
                new MenuProductCreate(1L, 1L, 1L),
                new MenuProductCreate(2L, 2L, 2L)
        );
        MenuCreate menuCreate = new MenuCreate(null, null, null, menuProductCreates);
        Products products = new Products(Arrays.asList(new Product("A", new Price(1))));

        // when & then
        assertThatExceptionOfType(ProductNotExistException.class)
                .isThrownBy(() -> Menu.create(menuCreate, null, products));
    }

    @Test
    @DisplayName("메뉴의 가격보다 금액이 더 싸면 MenuCheapException이 발생한다")
    void 메뉴의_가격보다_금액이_더_싸면_MenuCheapException이_발생한다() {
        // given
        List<MenuProductCreate> menuProductCreates = Arrays.asList(
                new MenuProductCreate(1L, 1L, 1L),
                new MenuProductCreate(2L, 2L, 2L)
        );
        MenuCreate menuCreate = new MenuCreate(null, new Price(100), null, menuProductCreates);
        Products products = new Products(
                Arrays.asList(
                        new Product(1L,"A", new Price(1)),
                        new Product(2L,"A", new Price(2))
                )
        );

        // when & then
        assertThatExceptionOfType(MenuCheapException.class)
                .isThrownBy(() -> Menu.create(menuCreate, null, products));
    }

    @Test
    @DisplayName("정상적인 등록")
    void 정상적인_등록() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "MENU_GROUP");
        List<MenuProductCreate> menuProductCreates = Arrays.asList(
                new MenuProductCreate(1L, 1L, 1L),
                new MenuProductCreate(2L, 2L, 2L)
        );

        List<Product> productList = Arrays.asList(
                new Product(1L, "A", new Price(1)),
                new Product(2L, "A", new Price(2))
        );

        MenuCreate menuCreate = new MenuCreate("NAME_OF_MENU", new Price(1), null, menuProductCreates);
        Products products = new Products(productList);

        // when
        Menu menu = Menu.create(menuCreate, menuGroup, products);

        // when & then
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getName()).isEqualTo(menuCreate.getName());
        assertThat(menu.getPrice()).isEqualTo(menuCreate.getPrice());
        assertThat(menu.getMenuProducts())
                .map(item -> item.getProduct())
                .containsExactlyElementsOf(productList);
        assertThat(menu.getMenuProducts())
                .map(item -> item.getMenu())
                .containsOnly(menu);
    }
}