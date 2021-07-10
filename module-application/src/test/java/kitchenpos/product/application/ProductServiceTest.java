package kitchenpos.product.application;

import static kitchenpos.util.TestDataSet.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 정상 생성 케이스")
    void create() {
        //given
        given(productRepository.save(any())).willReturn(강정치킨);

        //when
        ProductResponse result = productService.create(new ProductRequest(강정치킨.getName(), 강정치킨.getPrice()));

        // then
        assertThat(result.getName()).isEqualTo(강정치킨.getName());
        assertThat(result.getPrice()).isEqualTo(강정치킨.getPrice());

        verify(productRepository, times(1)).save(any());
    }

}
