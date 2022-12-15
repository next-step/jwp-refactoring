package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 허니콤보;
    private Product 레드윙;

    @BeforeEach
    void setUp() {
        허니콤보 = Product.of(1L, "허니콤보", BigDecimal.valueOf(18000));
        레드윙 = Product.of(2L, "레드윙", BigDecimal.valueOf(19000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productDao.save(any())).thenReturn(허니콤보);

        Product result = productService.create(허니콤보);

        assertThat(result).isEqualTo(허니콤보);
    }

    @DisplayName("상품 생성 시 가격이 null 이면 예외가 발생한다.")
    @Test
    void createException() {
        Product product = Product.of(1L, "product", null);

        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 시 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createException2() {
        Product product = Product.of(1L, "product", BigDecimal.valueOf(-1));

        Assertions.assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productDao.findAll()).thenReturn(Arrays.asList(허니콤보, 레드윙));

        List<Product> results = productService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(허니콤보, 레드윙)
        );
    }
}
