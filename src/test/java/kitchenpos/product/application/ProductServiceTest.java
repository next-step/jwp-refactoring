package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        ProductRequest request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(15_000));

        when(productRepository.save(any())).thenReturn(상품_생성(1L, "후라이드치킨", 15_000));

        // when
        ProductResponse response = productService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getProductId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(request.getPrice())
        );
    }

    @DisplayName("가격은 0 이상이여아 한다.")
    @Test
    void createProduct1() {
        // given
        ProductRequest request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(-1_000));

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void listProduct() {
        // given
        Product product1 = 상품_생성("후라이드치킨", 15_000);
        Product product2 = 상품_생성("양념치킨", 15_000);
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> responses = productService.list();

        // then
        assertThat(responses).hasSize(products.size());
    }
}
