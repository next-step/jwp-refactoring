package kitchenpos.product.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 일급 컬렉션 객체 테스트")
class ProductsTest {

    private Product 짜장면;
    private Product 짬뽕;

    @BeforeEach
    void setUp() {
        짜장면 = new Product(1L, "짜장면", new BigDecimal(7000));
        짬뽕 = new Product(2L, "짬뽕", new BigDecimal(8000));
    }

    @Test
    void 상품_일급_컬렉션_객체_생성() {
        assertThat(new Products(Arrays.asList(짜장면, 짬뽕))).isEqualTo(new Products(Arrays.asList(짜장면, 짬뽕)));
    }

    @Test
    void 상품이_포함되어있는_상품인지_확인() {
        Products products = new Products(Arrays.asList(짜장면));
        assertThat(products.contains(짜장면)).isTrue();
        assertThat(products.contains(짬뽕)).isFalse();
    }

    @Test
    void 메뉴_상품_수량을_이용하여_총액_구하기() {
        BigDecimal expected = BigDecimal.valueOf(15000);
        List<MenuProductRequest> menuProductRequests = Arrays.asList(new MenuProductRequest(1L, 1), new MenuProductRequest(2L, 1));
        Products products = new Products(Arrays.asList(짜장면, 짬뽕));
        BigDecimal actual = products.calcProductsPrice(menuProductRequests);
        assertThat(actual.compareTo(expected)).isEqualTo(0);
    }

    @Test
    void 예상하는_메뉴_상품_사이즈가_아닌경우_에러발생() {
        Products products = new Products(Arrays.asList(짜장면));
        assertThatThrownBy(() -> products.checkProductsSize(2)).isInstanceOf(IllegalArgumentException.class);
    }
}