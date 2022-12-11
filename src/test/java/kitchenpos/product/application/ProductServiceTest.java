package kitchenpos.product.application;

import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static kitchenpos.product.domain.ProductTestFixture.generateProductRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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

    private Product 감자튀김;
    private Product 콜라;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        ProductRequest productRequest = generateProductRequest(감자튀김.getName(), 감자튀김.getPrice());
        Product product = productRequest.toProduct();
        given(productRepository.save(product)).willReturn(감자튀김);

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertAll(
                () -> assertThat(productResponse.getId()).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo(감자튀김.getName().value()),
                () -> assertThat(productResponse.getPrice()).isEqualTo(감자튀김.getPrice().value())
        );
    }

    @DisplayName("가격이 비어있는 상품은 생성할 수 없다.")
    @Test
    void createProductThrowErrorWhenPriceIsNull() {
        // given
        ProductRequest productRequest = generateProductRequest(감자튀김.getName().value(), null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest))
                .withMessage(ErrorCode.가격은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-1000, -2000})
    void createProductThrowErrorWhenPriceIsSmallerThenZero(long price) {
        // given
        ProductRequest productRequest = generateProductRequest(감자튀김.getName().value(), BigDecimal.valueOf(price));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(productRequest))
                .withMessage(ErrorCode.가격은_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // given
        List<Product> products = Arrays.asList(감자튀김, 콜라);
        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> findProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts.stream().map(ProductResponse::getName))
                        .containsExactly(감자튀김.getName().value(), 콜라.getName().value())
        );
    }
}
