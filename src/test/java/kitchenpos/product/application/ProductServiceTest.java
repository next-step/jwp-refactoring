package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 양념치킨;
    private Product 후라이드치킨;

    @BeforeEach
    void setUp() {
        양념치킨 = new Product("양념치킨", new Price(BigDecimal.valueOf(2_000)));
        후라이드치킨 = new Product("후라이드치킨", new Price(BigDecimal.valueOf(18_000)));
    }

    @Test
    void 상품을_등록_할_수_있다() {
        ProductRequest request = ProductRequest.of("양념치킨", BigDecimal.valueOf(2_000));
        given(productRepository.save(양념치킨)).willReturn(양념치킨);

        ProductResponse response = productService.create(request);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(양념치킨.getId()),
                () -> assertThat(response.getName()).isEqualTo(양념치킨.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(양념치킨.getPrice().value())
        );
    }

    @Test
    void 가격이_존재하지_않는_상품은_등록할_수_없다() {
        ProductRequest product = ProductRequest.of("반반치킨", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1})
    void 상품의_가격은_0원_이상이어야_한다(int price) {
        ProductRequest product = ProductRequest.of("반반치킨", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        given(productRepository.findAll()).willReturn(Arrays.asList(양념치킨, 후라이드치킨));

        List<ProductResponse> products = productService.list();

        assertThat(products).hasSize(2);
        assertThat(products.stream().map(ProductResponse::getId))
                .contains(양념치킨.getId(), 후라이드치킨.getId());
    }
}
