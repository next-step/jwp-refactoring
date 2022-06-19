package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;

    @BeforeEach
    void setUp() {
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create01() {
        // given
        ProductRequest productRequest = ProductRequest.of("우아한_초밥_1", BigDecimal.valueOf(10_000));

        given(productRepository.save(any(Product.class))).willReturn(우아한_초밥_1);

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse).isEqualTo(ProductResponse.from(우아한_초밥_1));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다. (null)")
    @ParameterizedTest
    @NullSource
    void create02(BigDecimal price) {
        // given
        ProductRequest productRequest = ProductRequest.of("우아한_초밥_1", price);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다. (0미만)")
    @ParameterizedTest
    @ValueSource(longs = {-1, -100})
    void create03(long price) {
        // given
        ProductRequest productRequest = ProductRequest.of("우아한_초밥_1", BigDecimal.valueOf(price));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(productRepository.findAll()).willReturn(Lists.newArrayList(우아한_초밥_1, 우아한_초밥_2));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).containsExactly(ProductResponse.from(우아한_초밥_1), ProductResponse.from(우아한_초밥_2));
    }
}