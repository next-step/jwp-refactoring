package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 돼지고기;
    private Product 공기밥;

    @BeforeEach
    void setUp() {
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
    }

    @DisplayName("Product 를 등록한다.")
    @Test
    void create1() {
        // given
        ProductRequest productRequest = ProductRequest.of("돼지고기", BigDecimal.valueOf(9_000));

        given(productRepository.save(any(Product.class))).willReturn(돼지고기);

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertThat(productResponse).isEqualTo(ProductResponse.from(돼지고기));
    }

    @DisplayName("Product 가격이 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        ProductRequest productRequest = ProductRequest.of("돼지고기", null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("Product 가격이 음수(0 미만) 이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        ProductRequest productRequest = ProductRequest.of("돼지고기", BigDecimal.valueOf(wrongPrice));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest));
    }

    @DisplayName("")
    @Test
    void findList() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(돼지고기, 공기밥));

        // when
        List<ProductResponse> productResponses = productService.list();

        // then
        assertThat(productResponses).containsExactly(ProductResponse.from(돼지고기), ProductResponse.from(공기밥));
    }
}