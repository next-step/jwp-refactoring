package kitchenpos.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

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
}