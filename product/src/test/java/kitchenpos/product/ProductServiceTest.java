package kitchenpos.product;

import static kitchenpos.product.ProductFixture.createProductRequest;
import static kitchenpos.product.ProductFixture.강정치킨;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 생성")
    void createProduct() {
        //given
        when(productRepository.save(any())).thenReturn(강정치킨);
        ProductRequest productRequest = createProductRequest(강정치킨);

        //when
        ProductResponse productResponse = productService.create(productRequest);

        //then
        Assertions.assertThat(productResponse).isEqualTo(ProductResponse.from(강정치킨));
    }

    @Test
    @DisplayName("상품 목록 조회")
    void productList() {
        //given
        when(productRepository.findAll()).thenReturn(Collections.singletonList(강정치킨));

        //when
        List<ProductResponse> list = productService.list();

        //then
        Assertions.assertThat(list)
            .hasSize(1)
            .containsExactly(ProductResponse.from(강정치킨));
    }
}