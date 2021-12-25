package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.ProductService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Product product2;

    private ProductRequest productRequest;
    private ProductRequest productRequestZeroPrice;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "상품1", 3500);
        product2 = new Product(2L, "상품2", 1500);

        productRequest = new ProductRequest(1L, "상품1", 3500);
        productRequestZeroPrice = new ProductRequest(3L, "상품3", null);
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createProductTest() {
        when(productRepository.save(any())).thenReturn(product);

        // when
        final ProductResponse createdProduct = productService.create(productRequest);

        // then
        assertAll(
                () -> assertThat(createdProduct.getName()).isEqualTo("상품1"),
                () -> assertThat(createdProduct.getPrice()).isEqualTo(new BigDecimal(3500))
        );
    }

    @DisplayName("상품의 가격은 존재하고 0원 초과여야 한다.")
    @Test
    void createProductExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final ProductResponse createdProduct = productService.create(productRequestZeroPrice);

        // then
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 가져올 수 있다.")
    @Test
    void getProductsTest() {
        when(productRepository.findAll()).thenReturn(Lists.newArrayList(product, product2));

        // when
        final List<ProductResponse> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products.get(0).getName()).isEqualTo(product.getName()),
                () -> assertThat(products.get(1).getName()).isEqualTo(product2.getName())
        );
    }
}