package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import kitchenpos.dao.*;
import kitchenpos.domain.*;

@DisplayName("상품 관련 테스트")
class ProductServiceTest {
    private ProductDao productDao;
    private ProductService productService;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        productService = new ProductService(productDao);

        product1 = Product.of(1L, "짜장면", BigDecimal.valueOf(10000L));
        product2 = Product.of(2L, "짬뽕", BigDecimal.valueOf(12000L));
        product3 = Product.of(3L, "탕수육", BigDecimal.valueOf(30000L));
    }

    @DisplayName("상품 생성하기")
    @Test
    void createTest() {
        when(productDao.save(product1)).thenReturn(product1);
        assertThat(productService.create(product1)).isEqualTo(product1);
    }

    @DisplayName("상품 금액이 음수이면 예외 발생")
    @Test
    void exceptionTest1() {
        Product badProduct = Product.of(4L, "썩은 탕수육", BigDecimal.valueOf(-30000L));
        assertThatThrownBy(
            () -> productService.create(badProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성시 금액 정보가 없다면 예외 발생")
    @Test
    void exceptionTest2() {
        Product nullPriceProduct = Product.of(4L, "썩은 탕수육", null);
        assertThatThrownBy(
            () -> productService.create(nullPriceProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록 조회시 저장된 상품 목록 얻기")
    @Test
    void listTest() {
        when(productDao.findAll()).thenReturn(Lists.newArrayList(product1, product2, product3));
        assertThat(productService.list()).isEqualTo(Lists.newArrayList(product1, product2, product3));
    }
}
