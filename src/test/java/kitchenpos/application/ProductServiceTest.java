package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록() {
        // given
        Product 상품 = new Product();
        상품.setId(1L);
        상품.setName("치킨");
        상품.setPrice(new BigDecimal("18000"));
        given(productDao.save(상품)).willReturn(상품);

        // when
        Product 등록_결과 = productService.create(상품);

        // then
        assertThat(등록_결과).isEqualTo(상품);

    }
    
    @DisplayName("상품 등록시 가격은 필수여야한다 - 예외처리")
    @Test
    void 상품_등록_가격_필수() {
        // given
        Product 가격없는_상품 = new Product();
        가격없는_상품.setId(1L);
        가격없는_상품.setName("치킨");
        가격없는_상품.setPrice(null);

        // when, then
        assertThatThrownBy(() -> {
            productService.create(가격없는_상품);
        }).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("상품 등록시 가격은 0원 이상이어야한다 - 예외처리")
    @Test
    void 상품_등록_가격_0원_이상() {
        // given
        Product 마이너스_가격_상품 = new Product();
        마이너스_가격_상품.setId(1L);
        마이너스_가격_상품.setName("치킨");
        마이너스_가격_상품.setPrice(new BigDecimal("-6000"));

        // when, then
        assertThatThrownBy(() -> {
            productService.create(마이너스_가격_상품);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        // given
        Product 첫번째_상품 = new Product();
        첫번째_상품.setId(1L);
        첫번째_상품.setName("치킨");
        첫번째_상품.setPrice(new BigDecimal("18000"));
        
        Product 두번째_상품 = new Product();
        두번째_상품.setId(2L);
        두번째_상품.setName("삼겹살");
        두번째_상품.setPrice(new BigDecimal("20000"));
        
        given(productDao.findAll()).willReturn(Arrays.asList(첫번째_상품, 두번째_상품));

        // when
        List<Product> 상품_목록 = productService.list();

        // then
        assertThat(상품_목록).containsExactly(첫번째_상품, 두번째_상품);
    }

}
