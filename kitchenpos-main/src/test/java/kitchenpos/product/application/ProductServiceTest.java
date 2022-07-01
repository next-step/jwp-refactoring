package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관련 기능")
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // given
        Product product = 상품_등록(1L, "강정치킨", 17000);
        given(productRepository.save(any())).willReturn(product);

        // when
        ProductResponse createProduct = productService.create(상품_등록_요청("강정치킨", 17000));

        // then
        assertThat(createProduct).isNotNull();
    }

    @ParameterizedTest(name = "상품 가격이 0미만이면 등록 실패한다.")
    @ValueSource(ints = {-100})
    void createProductWithInValidPrice(int price) {
        // when-then
        assertThatThrownBy(() -> 상품_등록(1L, "강정치킨", price)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void getProduct() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(상품_등록(1L, "강정치킨", 17000)));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).isNotNull();
    }

    public static Product 상품_등록(Long id, String name, Integer price) {
        return Product.of(name, price);
    }

    public static ProductRequest 상품_등록_요청(String name, Integer price) {
        return ProductRequest.of(name, price);
    }
}
