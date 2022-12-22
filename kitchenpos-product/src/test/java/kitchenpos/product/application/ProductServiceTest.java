package kitchenpos.product.application;

import kitchenpos.global.message.PriceMessage;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        this.product = ProductFixture.후라이드;
    }

    @Test
    @DisplayName("상품 등록시 등록에 성공하고 상품 정보를 반환한다")
    void createProductThenReturnProductInfoTest() {
        // given
        ProductCreateRequest request = ProductCreateRequest.of("후라이드", 12_000L);
        BDDMockito.given(productRepository.save(ArgumentMatchers.any())).willReturn(request.toProduct());

        // when
        ProductResponse response = productService.createProduct(request);

        // then
        BDDMockito.then(productRepository).should(Mockito.times(1)).save(ArgumentMatchers.any());
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(request.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(request.getPrice())
        );
    }

    @Test
    @DisplayName("상품 등록시 상품 가격이 누락된경우 예외처리되어 등록에 실패한다")
    void createProductThenThrownByEmptyPriceTest() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("후라이드", null);

        // when
        Assertions.assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PriceMessage.CREATE_ERROR_PRICE_MUST_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("상품 등록시 상품 가격이 0원 미만인경우 예외처리되어 등록에 실패한다")
    void createProductThenThrownByLessThanZeroPriceTest() {
        // given
        ProductCreateRequest request = ProductCreateRequest.of("후라이드", -1L);

        // when
        Assertions.assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PriceMessage.CREATE_ERROR_PRICE_MUST_BE_GREATER_THAN_ZERO.message());
    }

    @Test
    @DisplayName("상품 목록 조회시 등록된 상품 목록을 반환한다")
    void findAllProductsTest() {
        // given
        BDDMockito.given(productRepository.findAll()).willReturn(Arrays.asList(product));

        // when
        List<ProductResponse> productResponses = productService.findAll();

        // then
        BDDMockito.then(productRepository).should(Mockito.times(1)).findAll();
        Assertions.assertThat(productResponses).hasSize(1);
    }
}
