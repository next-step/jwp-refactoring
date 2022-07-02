package kitchenpos.product.application;

import static kitchenpos.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        ProductRequest productRequest = 상품_요청_생성("피자", 25000);
        given(productRepository.save(any())).willReturn(productRequest.toProduct());

        //when
        ProductResponse productResponse = productService.create(productRequest);

        //then
        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(productRequest.getName());
        assertThat(productResponse.getPrice()).isEqualTo(productRequest.getPrice());
    }

    @DisplayName("가격이 0 미만인 상품은 등록에 실패한다.")
    @Test
    void create_invalidPrice() {
        //given
        ProductRequest productRequest = 상품_요청_생성("피자", -1);

        //when & then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(productRepository.findAll()).willReturn(
                Arrays.asList(상품_생성(1L, "피자", 25000), 상품_생성(2L, "파스타", 15000)));

        //when
        List<ProductResponse> productResponses = productService.list();

        //then
        assertThat(productResponses).hasSize(2);
    }
}
