package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("상품(제품)을 만든다.")
    void createProduct() {
        // given
        final Product 강정치킨 = new Product(1L, "강정치킨", Price.of(17_000L));
        final ProductResponse expected = 강정치킨.toProductResponse();
        when(productRepository.save(any())).thenReturn(강정치킨);
        // when
        final ProductResponse actual = productService.create(new ProductRequest("강정치킨", 17_000L));
        // given
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
        );
    }

    @Test
    @DisplayName("상품(제품)의 가격이 0보다 작으면 예외가 발생한다.")
    void createNegativePriceProduct() {
        // when && given
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new ProductRequest("음수치킨", -100L)));
    }

    @ParameterizedTest(name = "상품(제품)의 가격이 비어있다면 예외가 발생한다.")
    @NullSource
    void createEmptyPriceProduct(Long empty) {
        // when && given
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new ProductRequest("비어있는치킨", empty)));
    }

    @Test
    @DisplayName("상품(제품)들을 조회한다.")
    void searchProducts() {
        // given
        final Product product = new Product(1L, "제품", Price.of(1_000L));
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product));
        // when
        final List<ProductResponse> actual = productService.list();
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).hasSize(2)
        );
    }
}
