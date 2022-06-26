package kitchenpos.application;

import kitchenpos.domain.ProductEntity;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 목록_조회() {
        // given
        ProductEntity 팔당보쌈 = new ProductEntity(1L, "팔당보쌈", 28_000L);
        ProductEntity 파파존스_피자 = new ProductEntity(2L, "파파존스 피자", 22_000L);
        given(productRepository.findAll()).willReturn(Arrays.asList(팔당보쌈, 파파존스_피자));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}
