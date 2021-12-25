package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.ProductRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록() {
        // given
        Product 상품 = Product.of("치킨", 18000);
        given(productRepository.save(any())).willReturn(상품);

        // when
        ProductResponse 등록_결과 = productService.create(ProductRequest.from(상품));

        // then
        assertThat(등록_결과).isEqualTo(ProductResponse.from(상품));

    }
    
    
    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        // given
        Product 첫번째_상품 = Product.of("치킨", 18000);
        Product 두번째_상품 = Product.of("삼겹살", 20000);
        
        given(productRepository.findAll()).willReturn(Arrays.asList(첫번째_상품, 두번째_상품));
    
        // when
        List<ProductResponse> 상품_목록 = productService.list();
    
        // then
        assertThat(상품_목록).containsExactly(ProductResponse.from(첫번째_상품), ProductResponse.from(두번째_상품));
    }
    
}
