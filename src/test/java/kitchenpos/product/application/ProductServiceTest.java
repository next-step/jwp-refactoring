package kitchenpos.product.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
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

@DisplayName("ProductService 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        Product product = new Product(new Name("떡볶이"), new Price(BigDecimal.valueOf(3_000)));
        when(productRepository.save(product)).thenReturn(product);
        ProductRequest request = ProductRequest.of(product.getName().value(), product.getPrice().value());

        // when
        ProductResponse result = productService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(product.getId()),
                () -> assertThat(result.getName()).isEqualTo(product.getName().value()),
                () -> assertThat(result.getPrice()).isEqualTo(product.getPrice().value())
        );
    }

    @DisplayName("상품 가격이 null이면 예외가 발생한다.")
    @Test
    void createNullPriceProductionException() {
        // when & then
        assertThatThrownBy(() -> productService.create(ProductRequest.of("떡볶이", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 0 보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceProductionException(int input) {
        // when & then
        assertThatThrownBy(() -> productService.create(ProductRequest.of("떡볶이", BigDecimal.valueOf(input))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회할 수 있다.")
    @Test
    void findAllProduct() {
        // given
        Product product = new Product(1L, new Name("떡볶이"), new Price(BigDecimal.valueOf(3_000)));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<ProductResponse> results = productService.findAll();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(product.getId()),
                () -> assertThat(results.get(0).getName()).isEqualTo(product.getName().value()),
                () -> assertThat(results.get(0).getPrice()).isEqualTo(product.getPrice().value())
        );
    }
}
