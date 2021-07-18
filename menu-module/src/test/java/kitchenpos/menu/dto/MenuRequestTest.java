package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 요청 테스트")
class MenuRequestTest {

    private MenuRequest menuRequest;

    @BeforeEach
    void setUp() {
        List<MenuProductRequest> menuProductRequests = Arrays.asList(
                new MenuProductRequest(1L, 1),
                new MenuProductRequest(2L, 1));
        menuRequest = new MenuRequest("짜장면 탕수육 메뉴", new BigDecimal(19000), 1L, menuProductRequests);
    }

    @Test
    void 메뉴_요청_객체를_이용하여_메뉴_entity_생성() {
        Menu menu = menuRequest.toMenu();
        Menu expected = new Menu("짜장면 탕수육 메뉴", new BigDecimal(19000), 1L);
        assertThat(menu.getName()).isEqualTo(expected.getName());
        assertThat(menu.getPrice()).isEqualTo(expected.getPrice());
        assertThat(menu.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
    }

    @Test
    void 메뉴_요청_객체에_포함되어있는_메뉴_상품_리스트를_이용하여_메뉴_상품_entity_생성() {
        List<MenuProduct> menuProducts = menuRequest.toMenuProducts();
        assertThat(menuProducts.size()).isEqualTo(2);
        List<MenuProduct> expected = Arrays.asList(
                new MenuProduct(1L, 1),
                new MenuProduct( 2L, 1)
        );
        assertThat(menuProducts).containsExactlyElementsOf(expected);
    }

    @Test
    void 메뉴_요청_객체에_포함되어있는_메뉴_상품_리스트에서_상품_아이디_리스트_추출() {
        List<Long> actual = menuRequest.toProductIds();
        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual).containsExactly(1L, 2L);
    }

//    @Test
//    void 메뉴_상품_수량을_이용하여_총액_구하기() {
//
//        Product 짜장면 = new Product(1L, "짜장면", new BigDecimal(7000));
//        Product 탕수육 = new Product(2L, "탕수육", new BigDecimal(12000));
//        BigDecimal expected = BigDecimal.valueOf(19000);
//        Products products = new Products(Arrays.asList(짜장면, 탕수육));
//        BigDecimal actual = menuRequest.calcProductsPrice(products);
//        assertThat(actual.compareTo(expected)).isEqualTo(0);
//    }
}
