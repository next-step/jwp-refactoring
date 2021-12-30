package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    
    @DisplayName("상품을 등록한다.")
    @Test
    void saveProduct() {
        final Product 상품 = TestProductFactory.상품_생성돰(1L, "상품", 5000);

        given(productRepository.save(any())).willReturn(상품);

        final ProductResponse actual = productService.create(TestProductFactory.상품_요청("상품", 5000));

        TestProductFactory.상품_생성됨(actual, 상품);
    }

    @DisplayName("등록한 상품을 조회한다.")
    @Test
    void findProducts() {
        final List<Product> 상품_목록 = TestProductFactory.상품_목록_조회됨(10);

        given(productRepository.findAll()).willReturn(상품_목록);

        final List<ProductResponse> actual = productService.list();

        TestProductFactory.상품_목록_확인됨(actual, 상품_목록);
    }
}
