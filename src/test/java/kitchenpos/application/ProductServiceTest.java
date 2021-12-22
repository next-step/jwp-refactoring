package kitchenpos.application;

import static kitchenpos.acceptance.step.ProductAcceptanceStep.양념치킨;
import static kitchenpos.application.fixture.ProductFixture.후리이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관리 기능")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName(" `상품`을 등록할 수 있다.")
    void 상품_등록() {
        // given
        ProductRequest 양념치킨 = 양념치킨();
        given(productRepository.save(any())).willReturn(양념치킨.toProduct());

        // when
        ProductResponse 등록된_상품 = productService.create(양념치킨);

        // then
        상품이_등록_검증(등록된_상품);
    }

    @Test
    @DisplayName("`상품`목록을 조회 할 수 있다.")
    void 상품_목록_조회() {
        // given
        given(productRepository.findAll()).willReturn(Collections.singletonList(후리이드치킨(1L)));

        // when
        List<ProductResponse> 상품목록 = productService.list();

        //
        상품목록_조회됨(상품목록);
    }


    private void 상품목록_조회됨(List<ProductResponse> 상품목록) {
        assertThat(상품목록).isNotEmpty();
    }

    private void 상품이_등록_검증(ProductResponse 등록된_상품) {
        assertThat(등록된_상품).isNotNull();
    }
}
