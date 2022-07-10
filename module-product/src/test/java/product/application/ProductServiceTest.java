package product.application;

import product.repository.ProductRepository;
import product.application.ProductService;
import product.domain.Product;
import product.dto.ProductRequest;
import product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product 상품;

    @BeforeEach
    void setUp() {
        상품 = new Product("치킨", BigDecimal.valueOf(3000));
    }

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        ProductRequest request = new ProductRequest("치킨", BigDecimal.valueOf(3000));
        given(productRepository.save(any())).willReturn(상품);

        // when
        ProductResponse response = productService.create(request);

        // then
        assertThat(response.getName()).isEqualTo("치킨");
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        // given
        given(productRepository.findAll()).willReturn(Collections.singletonList(상품));

        // when
        List<ProductResponse> responses = productService.list();

        // then
        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.stream().map(ProductResponse::getName)).containsExactly("치킨")
        );
    }

    @Test
    void 상품_가격이_0보다_작은경우() {
        // given
        ProductRequest request = new ProductRequest("치킨", BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_빈값인_경우() {
        // given
        ProductRequest request = new ProductRequest("치킨", null);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(NullPointerException.class);
    }
}
