package kitchenpos.product.application;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    public static final Product 불고기버거 = Product.of("불고기버거", BigDecimal.valueOf(1500));
    public static final Product 새우버거 = Product.of("새우버거", BigDecimal.valueOf(2000));
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;


    @Test
    @DisplayName("상품 추가")
    void create() {
        // given
        given(productRepository.save(any()))
                .willReturn(불고기버거);
        // when
        final ProductResponse 제품_생성 = productService.create(불고기버거);
        // then
        assertThat(제품_생성).isInstanceOf(ProductResponse.class);
    }

    @Test
    @DisplayName("상품 전체 조회")
    void list() {
        // given
        given(productRepository.findAll())
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        final List<ProductResponse> list = productService.list();
        // then
        assertThat(list).hasSize(2);
    }
}
