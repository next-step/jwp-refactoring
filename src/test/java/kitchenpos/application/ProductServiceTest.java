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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        // given
        Product product = new Product(1L, "아메리카노", BigDecimal.valueOf(3_000));
        when(productDao.save(product)).thenReturn(product);

        // when
        Product result = productService.create(product);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(product.getId()),
            () -> assertThat(result.getName()).isEqualTo(product.getName()),
            () -> assertThat(result.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @DisplayName("상품 가격이 올바르지 않으면 예외가 발생한다.")
    @Test
    void createNullPriceProductionException() {
        // given
        Product product = new Product(1L, "아메리카노", null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 0 보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceProductionException(int input) {
        // given
        Product product = new Product(1L, "아메리카노", BigDecimal.valueOf(input));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProduct() {
        // given
        Product product = new Product(1L, "아메리카노", BigDecimal.valueOf(3_000));
        when(productDao.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<Product> results = productService.list();

        // then
        assertAll(
            () -> assertThat(results).hasSize(1),
            () -> assertThat(results.get(0).getId()).isEqualTo(product.getId()),
            () -> assertThat(results.get(0).getName()).isEqualTo(product.getName()),
            () -> assertThat(results.get(0).getPrice()).isEqualTo(product.getPrice())
        );
    }
}
