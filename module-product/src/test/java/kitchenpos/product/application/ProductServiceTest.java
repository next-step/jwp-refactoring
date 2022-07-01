package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관련 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성할 수 있다")
    @Test
    void create() {
        // given
        ProductRequest request = new ProductRequest("초밥", BigDecimal.valueOf(20000));
        Product 예상값 = new Product(1L, "초밥", BigDecimal.valueOf(20000));
        given(productRepository.save(any(Product.class))).willReturn(예상값);

        // when
        ProductResponse 상품_생성_결과 = 상품_생성(request);

        // then
        상품_값_비교(상품_생성_결과, ProductResponse.of(예상값));
    }

    @DisplayName("상품을 생성할 수 있다 - 상품은 0원 이상의 가격을 가져야 한다")
    @Test
    void create_exception1() {
        // given
        ProductRequest request1 = new ProductRequest("초밥", null);

        // when && then
        assertThatThrownBy(() -> 상품_생성(request1))
                .isInstanceOf(IllegalArgumentException.class);

        // given
        ProductRequest request2 = new ProductRequest("초밥", null);

        // when && then
        assertThatThrownBy(() -> 상품_생성(request2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        List<Product> 예상값 = Arrays.asList(
                new Product(1L, "초밥", BigDecimal.valueOf(20000)),
                new Product(2L, "김밥", BigDecimal.valueOf(2000))
        );
        given(productRepository.findAll()).willReturn(예상값);

        // when
        List<ProductResponse> 상품_조회_결과 = productService.list();

        // then
        assertAll(
                () -> 상품_값_비교(상품_조회_결과.get(0), ProductResponse.of(예상값.get(0))),
                () -> 상품_값_비교(상품_조회_결과.get(1), ProductResponse.of(예상값.get(1)))
        );
    }

    private ProductResponse 상품_생성(ProductRequest request) {
        return productService.create(request);
    }

    private void 상품_값_비교(ProductResponse result, ProductResponse expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(expectation.getPrice())
        );
    }
}
