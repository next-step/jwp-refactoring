package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 햄버거;
    private Product 피자;

    @BeforeEach
    void init() {
        // given
        햄버거 = 상품_생성(1L, "햄버거", 9_500L);
        피자 = 상품_생성(2L, "피자", 21_000L);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        // given
        given(productRepository.save(any(Product.class))).willReturn(햄버거);

        // when
        ProductResponse savedProduct = productService.create(new ProductRequest(햄버거));

        // then
        assertAll(
            () -> assertThat(savedProduct).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(햄버거.getName()),
            () -> assertThat(savedProduct.getId()).isEqualTo(햄버거.getId())
        );
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void findAll() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(햄버거, 피자));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products.size()).isEqualTo(2);
    }

    public static Product 상품_생성(String name) {
        return new Product(name, null);
    }

    public static Product 상품_생성(String name, Long price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Product 상품_생성(Long id, String name, Long price) {
        return new Product(id, name, new BigDecimal(price));
    }
}
