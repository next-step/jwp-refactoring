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

    @DisplayName("create메서드에 생성을 원하는 Product 객체를 인자로 하여 호출하면, 생성된 객체를 반환한다.")
    @Test
    void createTest() {
        when(productDao.save(product1)).thenReturn(product1);
        assertThat(productService.create(product1)).isEqualTo(product1);
    }

    @DisplayName("create메서드에 금액이 음수인 Product 객체를 인자로 하여 호출하면, 예외를 던진다.")
    @Test
    void exceptionTest1() {
        Product badProduct = Product.of(4L, "썩은 탕수육", BigDecimal.valueOf(-30000L));
        assertThatThrownBy(
            () -> productService.create(badProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create메서드에 금액이 null인 Product 객체를 인자로 하여 호출하면, 예외를 던진다.")
    @Test
    void exceptionTest2() {
        Product nullPriceProduct = Product.of(4L, "썩은 탕수육", null);
        assertThatThrownBy(
            () -> productService.create(nullPriceProduct)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("list메서드를 호출하면, 기 생성된 Product 목록을 반환한다.")
    @Test
    void listTest() {
        when(productDao.findAll()).thenReturn(Lists.newArrayList(product1, product2, product3));
        assertThat(productService.list()).isEqualTo(Lists.newArrayList(product1, product2, product3));
    }
}
