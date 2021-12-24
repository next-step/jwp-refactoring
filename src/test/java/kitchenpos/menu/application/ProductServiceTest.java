package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.ProductService;
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
        Product 상품 = Product.of("치킨", new BigDecimal("18000"));
        given(productRepository.save(any())).willReturn(상품);

        // when
        ProductResponse 등록_결과 = productService.create(ProductRequest.from(상품));

        // then
        assertThat(등록_결과).isEqualTo(ProductResponse.from(상품));

    }
    
    @DisplayName("상품 등록시 가격은 필수여야한다 - 예외처리")
    @Test
    void 상품_등록_가격_필수() {
        // given
        Product 가격없는_상품 = Product.of("치킨", null);
    
        // when, then
        assertThatThrownBy(() -> {
            productService.create(ProductRequest.from(가격없는_상품));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("상품은 0원 이상이어야 합니다");
    }
    
    @DisplayName("상품 등록시 가격은 0원 이상이어야한다 - 예외처리")
    @Test
    void 상품_등록_가격_0원_이상() {
        // given
        Product 마이너스_가격_상품 = Product.of("치킨", new BigDecimal("-6000"));
    
        // when, then
        assertThatThrownBy(() -> {
            productService.create(ProductRequest.from(마이너스_가격_상품));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("상품은 0원 이상이어야 합니다");
    }
    
    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        // given
        Product 첫번째_상품 = Product.of("치킨", new BigDecimal("18000"));
        Product 두번째_상품 = Product.of("삼겹살", new BigDecimal("20000"));
        
        given(productRepository.findAll()).willReturn(Arrays.asList(첫번째_상품, 두번째_상품));
    
        // when
        List<ProductResponse> 상품_목록 = productService.list();
    
        // then
        assertThat(상품_목록).containsExactly(ProductResponse.from(첫번째_상품), ProductResponse.from(두번째_상품));
    }
    
}
