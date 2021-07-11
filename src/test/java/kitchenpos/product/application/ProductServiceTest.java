package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("주어진 상품을 저장하고, 저장된 객체를 리턴한다.")
    @Test
    void create_product() {
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
        ProductResponse productResponse = new ProductResponse(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product = new Product(1L, "후라이드", BigDecimal.valueOf(16000));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);
        ProductResponse actual = productService.create(productRequest);

        assertThat(actual).isEqualTo(productResponse);
    }

    @Test
    @DisplayName("상품저장시 가격이 없으면 예외를 던진다.")
    void create_product_with_no_price() {
        ProductRequest productRequest = new ProductRequest("반반치킨", null);

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품저장시 음수인 가격이 주어지면 예외를 던진다.")
    void create_product_with_negative_price() {
        ProductRequest productRequest = new ProductRequest("반반치킨", BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 상품을 조회한다")
    void list() {
        Product product1 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
        ProductResponse productResponse1 = new ProductResponse(product1);
        ProductResponse productResponse2 = new ProductResponse(product2);

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(product1, product2));
        List<ProductResponse> products = productService.list();

        assertThat(products).containsAll(Arrays.asList(productResponse1, productResponse2));
    }
}
