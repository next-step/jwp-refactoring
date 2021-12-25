package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.domain.MenuProductTest.getMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품 컬렉션 도메인 테스트")
public class MenuProductsTest {
    @DisplayName("메뉴 상품 컬렉션을 생성한다.")
    @Test
    void 메뉴_상품_컬렉션_생성() {
        // given
        MenuProduct 양념치킨_메뉴_상품 = getMenuProduct(1L, "양념치킨", 20000, 2L);
        MenuProduct 콜라_메뉴_상품 = getMenuProduct(2L, "콜라", 2000, 3L);
        List<MenuProduct> menuProductList = new ArrayList<>(Arrays.asList(양념치킨_메뉴_상품, 콜라_메뉴_상품));

        // when
        MenuProducts menuProducts = MenuProducts.of(menuProductList);

        // then
        assertThat(menuProducts.asList().size()).isEqualTo(2);
    }

    @DisplayName("메뉴 상품 컬렉션 전체합계를 계산한다.")
    @Test
    void 메뉴_상품_컬렉션_전체합계_계산() {
        // given
        MenuProduct 양념치킨_메뉴_상품 = getMenuProduct(1L, "양념치킨", 20000, 2L);
        MenuProduct 콜라_메뉴_상품 = getMenuProduct(2L, "콜라", 2000, 3L);
        List<MenuProduct> menuProductList = new ArrayList<>(Arrays.asList(양념치킨_메뉴_상품, 콜라_메뉴_상품));
        MenuProducts menuProducts = MenuProducts.of(menuProductList);

        // when
        BigDecimal menuProductsSum = menuProducts.sum();

        // then
        assertThat(menuProductsSum).isEqualTo(BigDecimal.valueOf(46000));
    }
}
