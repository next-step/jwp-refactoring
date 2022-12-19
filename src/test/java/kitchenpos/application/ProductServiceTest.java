package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Test
    void createProductTest() {
        // given
        Product product = new Product(1L, "삼겹살", BigDecimal.valueOf(12_000));
        when(productDao.save(product)).thenReturn(product);

        // when
        Product result = productService.create(product);

        // then
        checkForCreateProduct(result, product);
    }

    private void checkForCreateProduct(Product createProduct, Product sourceProduct) {
        assertAll(
                () -> assertThat(createProduct.getId()).isEqualTo(sourceProduct.getId()),
                () -> assertThat(createProduct.getName()).isEqualTo(sourceProduct.getName()),
                () -> assertThat(createProduct.getPrice()).isEqualTo(sourceProduct.getPrice())
        );
    }

    @DisplayName("상품 생성 테스트 - 올바르지 않는 가격인경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -1000 })
    void createProductTest2(int price) {
        // given
        Product product = new Product(1L, "삼겹살", BigDecimal.valueOf(price));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findAllProductsTest() {
        // given
        Product product = new Product(1L, "삼겹살", BigDecimal.valueOf(12_000));
        when(productDao.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<Product> result = productService.list();

        // then
        assertThat(result).hasSize(1)
                .containsExactly(product);
    }

}