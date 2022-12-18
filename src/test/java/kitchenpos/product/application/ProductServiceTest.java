package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.Product;
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

@DisplayName("상품 관련 비즈니스 테스트")
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
        Product product = new Product("잔치국수", BigDecimal.valueOf(3_000));
        when(productRepository.save(product)).thenReturn(product);
        ProductRequest productRequest = ProductRequest.of(product.getName().value(), product.getPrice().value());

        // when
        ProductResponse result = productService.create(productRequest);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(product.getId()),
            () -> assertThat(result.getName()).isEqualTo(product.getName().value()),
            () -> assertThat(result.getPrice()).isEqualTo(product.getPrice().value())
        );
    }

    @DisplayName("상품의 가격이 null이면 예외가 발생한다.")
    @Test
    void createProductNullPriceException() {
        // when & then
        assertThatThrownBy(() -> productService.create(ProductRequest.of("잔치국수", null)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0원보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -1000, -10000 })
    void crateProductUnderZeroPriceException(int price) {
        // when & then
        assertThatThrownBy(() -> productService.create(ProductRequest.of("잔치국수", BigDecimal.valueOf(price))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findAllProducts() {
        // given
        Product product = new Product("잔치국수", BigDecimal.valueOf(3_000));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<ProductResponse> result = productService.list();

        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(ProductResponse::getName))
                .containsExactly(product.getName().value())
        );
    }
}
