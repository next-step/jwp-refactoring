package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 일급 컬렉션 테스트")
class MenuProductsTest {


    private MenuProduct 짜장면;
    private MenuProduct 짬뽕;

    @BeforeEach
    void setUp() {
        짜장면 = new MenuProduct(1L, 1);
        짬뽕 = new MenuProduct(2L, 1);
    }

    @Test
    void 메뉴_상품_일급_컬렉션_테스트() {
        List<MenuProduct> menuProducts = Arrays.asList(짜장면, 짬뽕);
        assertThat(new MenuProducts(menuProducts)).isEqualTo(new MenuProducts(menuProducts));
    }

    @Test
    void 메뉴_상품_일급_컬렉션에_메뉴_상품이_포함되어_있는지_여부_확인() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(짜장면));
        assertThat(menuProducts.contains(짜장면)).isTrue();
        assertThat(menuProducts.contains(짬뽕)).isFalse();
    }

    @Test
    void 메뉴_상품_일급_컬렉션에_메뉴_상품_추가() {
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(짜장면));
        menuProducts.addMenuProduct(짬뽕);
        assertThat(menuProducts.contains(짜장면)).isTrue();
        assertThat(menuProducts.contains(짬뽕)).isTrue();
    }
}
