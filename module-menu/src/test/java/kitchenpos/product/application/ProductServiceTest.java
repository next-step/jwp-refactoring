package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.CannotCreateException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final Integer PRODUCT_PRICE = 10000;
    private static final String PRODUCT_NAME = "상품";
    private Product product;
    private ProductRequest productRequest;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product = Product.of(PRODUCT_NAME, PRODUCT_PRICE);
        productRequest = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {

        when(productRepository.save(any(Product.class)))
            .thenReturn(product);

        ProductResponse saved = productService.create(productRequest);

        assertAll(
            () -> assertThat(saved.getName()).isEqualTo(PRODUCT_NAME),
            () -> assertThat(saved.getPrice()).isEqualTo(BigDecimal.valueOf(PRODUCT_PRICE)));
    }


    @Test
    @DisplayName("상품 목록 조회")
    void list() {
        when(productRepository.findAll())
            .thenReturn(Arrays.asList(product));

        List<ProductResponse> products = productService.list();

        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 생성시 validate 체크")
    void validate() {
        assertThatThrownBy(() -> productService.create(new ProductRequest(null, PRODUCT_PRICE)))
            .isInstanceOf(CannotCreateException.class)
            .hasMessage("상품을 생성할 수 없습니다. 다시 입력해 주세요.");

        assertThatThrownBy(() -> productService.create(new ProductRequest(PRODUCT_NAME, null)))
            .isInstanceOf(CannotCreateException.class)
            .hasMessage("상품을 생성할 수 없습니다. 다시 입력해 주세요.");
    }
}