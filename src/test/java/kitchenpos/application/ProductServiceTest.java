package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.*;
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

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성하기")
    @Test
    void createTest() {
        when(productDao.save(후라이드치킨)).thenReturn(후라이드치킨);
        assertThat(productService.create(후라이드치킨)).isEqualTo(후라이드치킨);
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
        when(productDao.findAll()).thenReturn(Lists.newArrayList(후라이드치킨, 양념치킨, 반반치킨));
        assertThat(productService.list()).isEqualTo(Lists.newArrayList(후라이드치킨, 양념치킨, 반반치킨));
    }
}
