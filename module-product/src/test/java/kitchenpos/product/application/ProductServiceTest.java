package kitchenpos.product.application;

import kitchenpos.common.exception.NegativePriceException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
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

    private Product 짜장면;
    private Product 짬뽕;

    private ProductRequest 짜장면요청;
    private ProductRequest 가격0원상품요청;

    @BeforeEach
    void setUp() {
        짜장면 = new Product(1L, "짜장면", 3500);
        짬뽕 = new Product(2L, "짬뽕", 1500);

        짜장면요청 = new ProductRequest(1L, "짜장면", 3500);
        가격0원상품요청 = new ProductRequest(3L, "말도안되는상품", null);
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createProductTest() {
        when(productRepository.save(any())).thenReturn(짜장면);

        // when
        final ProductResponse createdProduct = 상품을_등록한다(짜장면요청);

        // then
        assertAll(
                () -> Assertions.assertThat(createdProduct.getName()).isEqualTo("짜장면"),
                () -> Assertions.assertThat(createdProduct.getPrice()).isEqualTo(new BigDecimal(3500))
        );
    }

    @DisplayName("상품의 가격은 존재하고 0원 초과여야 한다.")
    @Test
    void createProductExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final ProductResponse createdProduct = 상품을_등록한다(가격0원상품요청);

        // then
        }).isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("상품 목록을 가져올 수 있다.")
    @Test
    void getProductsTest() {
        when(productRepository.findAll()).thenReturn(Lists.newArrayList(짜장면, 짬뽕));

        // when
        final List<ProductResponse> products = 상품_목록을_조회한다();

        // then
        assertAll(
                () -> Assertions.assertThat(products.get(0).getName()).isEqualTo(짜장면.getName()),
                () -> Assertions.assertThat(products.get(1).getName()).isEqualTo(짬뽕.getName())
        );
    }

    private ProductResponse 상품을_등록한다(ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    private List<ProductResponse> 상품_목록을_조회한다() {
        return productService.list();
    }
}