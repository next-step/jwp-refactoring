package kitchenpos.product.application;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.product.domain.ProductTest.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관리 테스트")
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProductTest() {
        // given
        ProductRequest 후라이드_요청 = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
        given(productRepository.save(any())).willReturn(후라이드_상품);
        // when
        ProductResponse actual = productService.create(후라이드_요청);
        // then
        assertThat(actual).isEqualTo(new ProductResponse(actual.getId(), "후라이드", BigDecimal.valueOf(16000)));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 0
    })
    @DisplayName("상품 가격이 0원 이상인 상품 등록")
    void productPriceOverZeroTest(int price) {
        // given
        ProductRequest 양념치킨_요청 = new ProductRequest("양념치킨", BigDecimal.valueOf(price));
        given(productRepository.save(any())).willReturn(양념치킨_요청.toProduct());
        // when
        ProductResponse actual = productService.create(양념치킨_요청);
        // then
        assertThat(actual).isEqualTo(new ProductResponse(actual.getId(), "양념치킨", BigDecimal.valueOf(price)));
    }

    @Test
    @DisplayName("상품 가격은 0원 이상 이어야 한다.")
    void productPriceExceptionTest() {
        // given
        ProductRequest 요청_데이터 = new ProductRequest("양념치킨", BigDecimal.valueOf(-1));
        // when
        // then
        assertThatThrownBy(() -> productService.create(요청_데이터))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void findAllProductTest() {
        // given
        ProductResponse 상품_응답 = ProductResponse.of(후라이드_상품);
        given(productRepository.findAll())
                .willReturn(Collections.singletonList(후라이드_상품));
        // when
        List<ProductResponse> actual = productService.list();
        // then
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(상품_응답);
    }
}
