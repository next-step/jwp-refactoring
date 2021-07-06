package kitchenpos.product.application;

import static kitchenpos.util.TestDataSet.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.ProductRepository;
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
