package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 버팔로윙;
    private Product 치킨텐더;

    @BeforeEach
    void setUp() {
        버팔로윙 = new Product(1L, "버팔로윙", BigDecimal.valueOf(6_500));
        치킨텐더 = new Product(2L, "치킨텐더", BigDecimal.valueOf(5_900));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productDao.save(any())).thenReturn(버팔로윙);

        Product result = productService.create(버팔로윙);

        assertThat(result).isEqualTo(버팔로윙);
    }

    @DisplayName("상품 생성 시 가격이 null 이면 예외가 발생한다.")
    @Test
    void createException() {
        Product product = new Product(1L, "product", null);

        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 시 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createException2() {
        Product product = new Product(1L, "product", BigDecimal.valueOf(-1));

        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productDao.findAll()).thenReturn(Arrays.asList(버팔로윙, 치킨텐더));

        List<Product> results = productService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(버팔로윙, 치킨텐더)
        );
    }
}
