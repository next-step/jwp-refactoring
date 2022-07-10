package kitchenpos.product.application;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    private Product 지코바치킨;

    @BeforeEach
    void setUp() {
        지코바치킨 = new Product("지코바치킨", Price.from(20000));
    }

    @Test
    void 상품을_등록할_수_있다() {
        given(productRepository.save(any())).willReturn(지코바치킨);

        ProductResponse productResponse = productService.create(new ProductRequest(지코바치킨));

        assertAll(
                () -> assertThat(productResponse.getName()).isEqualTo(지코바치킨.getName()),
                () -> assertThat(productResponse.getPrice()).isEqualTo(지코바치킨.getPriceValue()),
                () -> assertThat(productResponse).isNotNull()
        );
    }

    @Test
    void 상품을_조회할_수_있다() {
        given(productRepository.findAll()).willReturn(Arrays.asList(지코바치킨));

        List<Product> 상품리스트 = productService.list();

        assertThat(상품리스트).isNotNull();
    }
}
