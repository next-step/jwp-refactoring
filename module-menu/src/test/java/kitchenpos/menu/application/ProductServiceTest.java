package kitchenpos.menu.application;

import static kitchenpos.menu.__fixture__.ProductTestFixture.상품_요청_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.infra.ProductRepository;
import kitchenpos.menu.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductService 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;
    private ProductRequest 후라이드치킨_상품_요청;

    @BeforeEach
    void setUp() {
        후라이드치킨_상품_요청 = 상품_요청_생성("후라이드치킨", BigDecimal.valueOf(16_000));
    }

    @Test
    @DisplayName("상품 등록 시 가격이 0보다 작을 경우 Exception")
    public void createPriceIsMinusException() {
        final ProductRequest 상품_요청 = 상품_요청_생성("후라이드치킨", BigDecimal.valueOf(-16_000));
        assertThatThrownBy(() -> productService.create(상품_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 시 가격이 Null일 경우 Exception")
    public void createPriceIsNullException() {
        final ProductRequest 상품_요청 = 상품_요청_생성("후라이드치킨", null);
        assertThatThrownBy(() -> productService.create(상품_요청)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 정상 등록")
    public void create() {
        given(productRepository.save(후라이드치킨_상품_요청.toProduct())).willReturn(후라이드치킨_상품_요청.toProduct());

        assertThat(productService.create(후라이드치킨_상품_요청).getId()).isEqualTo(후라이드치킨_상품_요청.toProduct().getId());
    }

    @Test
    public void list() {
        given(productRepository.findAll()).willReturn(Arrays.asList(후라이드치킨_상품_요청.toProduct()));

        assertThat(productService.list()).contains(후라이드치킨_상품_요청.toProduct());
    }
}
