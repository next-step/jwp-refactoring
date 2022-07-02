package kitchenpos.product.application;

import kitchenpos.product.appliaction.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.fixture.ProductFixture.치킨;
import static kitchenpos.product.fixture.ProductFixture.피자;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 등록시 정상 상품인 경우 정상 등록 된다")
    void create() {
        ProductRequest 요청_피자 = ProductRequest.of("피자", BigDecimal.valueOf(22_000));

        when(productRepository.save(any())).thenReturn(피자);

        ProductResponse 피자_상품_등록_결과 = productService.create(요청_피자);

        Assertions.assertThat(피자_상품_등록_결과).isEqualTo(ProductResponse.of(피자));
    }

    @Test
    @DisplayName("상품 등록시 가격이 0원인 경우 상품 등록이 실패된다.")
    void createFail() {
        ProductRequest 요청_치킨 = ProductRequest.of("치킨", BigDecimal.valueOf(-100));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(요청_치킨));
    }

    @Test
    @DisplayName("상품 목록 조회시 상품목록이 존재한다면 조회된다.")
    void find() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(피자, 치킨));

        List<ProductResponse> 상품_목록_조회_결과 = productService.list();

        assertAll(
                () -> Assertions.assertThat(상품_목록_조회_결과).hasSize(2),
                () -> Assertions.assertThat(상품_목록_조회_결과).containsExactly(ProductResponse.of(피자), ProductResponse.of(치킨))
        );
    }
}
