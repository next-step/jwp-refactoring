package kitchenpos.application;

import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_목록_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.application.product.ProductService;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Product")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    public void createProduct() {
        // Given
        final Product given = 상품_생성();
        given(productRepository.save(any(Product.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        Product actual = productService.create(given);

        // Then
        verify(productRepository).save(any(Product.class));
        assertThat(actual).isEqualTo(given);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    public void getProducts() {
        // Given
        final int generateProductCount = 5;
        List<Product> givenProducts = 상품_목록_생성(generateProductCount);
        given(productRepository.findAll()).willReturn(givenProducts);

        // When
        List<Product> actualProducts = productService.list();

        // Then
        verify(productRepository).findAll();
        assertThat(actualProducts).hasSize(generateProductCount);
    }
}
