package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Price;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product 후라이드치킨_상품;
    private Product 양념치킨_상품;
    private Product 간장치킨_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨_상품 = new Product(1L, "후라이드치킨", new Price(BigDecimal.valueOf(16_000L)));
        양념치킨_상품 = new Product(2L, "양념치킨", new Price(BigDecimal.valueOf(16_000L)));
        간장치킨_상품 = new Product(3L, "간장치킨", new Price(BigDecimal.valueOf(17_000L)));
    }

    @DisplayName("상품의 가격은 반드시 존재해야 한다.")
    @Test
    void 상품의_가격은_반드시_존재해야_한다() {
        // given
        ProductRequest 햄버거 = new ProductRequest("햄버거", null);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(햄버거));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        ProductRequest 햄버거 = new ProductRequest("햄버거", BigDecimal.valueOf(-1));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(햄버거));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품을_생성한다() {
        // given
        when(productRepository.save(any(Product.class))).thenReturn(후라이드치킨_상품);

        // when
        ProductResponse product = productService.create(new ProductRequest("후라이드치킨", BigDecimal.valueOf(16_000)));

        // then
        assertAll(() -> {
            assertThat(product.getId()).isEqualTo(1L);
            assertThat(product.getName()).isEqualTo("후라이드치킨");
            assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16_000L));
        });
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품을_조회한다() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(양념치킨_상품, 간장치킨_상품));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}
