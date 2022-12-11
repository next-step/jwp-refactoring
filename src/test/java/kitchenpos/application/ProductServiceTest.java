package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.ProductTestFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 짜장면;
    private Product 단무지;

    @BeforeEach
    void setUp() {
        짜장면 = createProduct(1L, "짜장면", BigDecimal.valueOf(8000L));
        단무지 = createProduct(2L, "단무지", BigDecimal.valueOf(0L));
    }

    @DisplayName("상품 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(productDao.save(짜장면)).thenReturn(단무지);

        // when
        Product product = productService.create(짜장면);

        // then
        assertThat(product.getId()).isNotNull();
    }

    @DisplayName("가격이 0원 미만인 상품을 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        Product product = createProduct(3L, "짜장면", BigDecimal.valueOf(-1000));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Product> expected = Arrays.asList(짜장면, 단무지);
        when(productDao.findAll()).thenReturn(expected);

        // when
        List<Product> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(expected.size()),
                () -> assertThat(actual).containsExactly(짜장면, 단무지)
        );
    }
}
