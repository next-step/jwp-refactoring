package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 짜장면;
    private Product 짬뽕;

    @BeforeEach
    void setUp() {
        짜장면 = new Product(1L, "짜장면", new BigDecimal(7000));
        짬뽕 = new Product(2L, "짬뽕", new BigDecimal(8000));
    }

    @Test
    void 상품_등록() {
        ProductRequest productRequest = new ProductRequest("짜장면", new BigDecimal(7000));
        when(productRepository.save(any(Product.class))).thenReturn(짜장면);

        ProductResponse actual = productService.create(productRequest);

        assertThat(actual.getId()).isEqualTo(짜장면.getId());
        assertThat(actual.getName()).isEqualTo(짜장면.getName());
        assertThat(actual.getPrice().compareTo(짜장면.getPrice().price())).isEqualTo(0);
    }

    @Test
    void 등록된_상품_목록_조회() {
        ProductResponse 짜장면_결과 = ProductResponse.of(짜장면);
        ProductResponse 짬뽕_결과 = ProductResponse.of(짬뽕);

        when(productRepository.findAll()).thenReturn(Arrays.asList(짜장면, 짬뽕));
        List<ProductResponse> products = productService.list();
        assertThat(products).containsExactly(짜장면_결과, 짬뽕_결과);
    }

    @Test
    void 등록된_상품_id_기준으로_조회() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(productRepository.findAllById(ids)).thenReturn(Arrays.asList(짜장면, 짬뽕));
        Products products = productService.findProductsByIds(ids);
        assertThat(products.contains(짜장면)).isTrue();
        assertThat(products.contains(짬뽕)).isTrue();
    }
}
