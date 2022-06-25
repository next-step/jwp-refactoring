package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductEntity;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성_성공() {
        // given
        ProductEntity 뿌링클윙 = new ProductEntity("뿌링클윙", BigDecimal.valueOf(20_000));
        given(productRepository.save(any(ProductEntity.class))).willReturn(뿌링클윙);

        // when
        ProductResponse saved = productService.create(new ProductRequest(뿌링클윙.getName(), 뿌링클윙.getUnitPrice()));

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(뿌링클윙.getName());
        assertThat(saved.getPrice()).isEqualTo(뿌링클윙.getUnitPrice());
    }

    @DisplayName("상품 생성에 실패한다.")
    @Test
    void 상품_생성_예외() {
        // when, then
        assertThatThrownBy(() -> productService.create(new ProductRequest("뿌링클순살", -7L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        ProductEntity 팔당보쌈 = new ProductEntity(1L, "팔당보쌈", 28_000L);
        ProductEntity 파파존스_피자 = new ProductEntity(2L, "파파존스 피자", 22_000L);
        given(productRepository.findAll()).willReturn(Arrays.asList(팔당보쌈, 파파존스_피자));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }

    public static Product 상품_생성(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
