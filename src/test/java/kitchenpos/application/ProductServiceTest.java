package kitchenpos.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.ProductTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest 짜장면요청;
    private ProductRequest 단무지요청;
    private Product 짜장면;
    private Product 단무지;

    @BeforeEach
    void setUp() {
        짜장면요청 = 짜장면요청();
        단무지요청 = 단무지요청();
        짜장면 = 상품생성(짜장면요청);
        단무지 = 상품생성(단무지요청);
    }

    @DisplayName("상품 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(productRepository.save(any())).thenReturn(짜장면);

        // when
        ProductResponse product = productService.create(짜장면요청);

        // then
        assertThat(product).isNotNull();
    }

    @DisplayName("가격이 0원 미만인 상품을 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        ProductRequest product = 상품( "짜장면", BigDecimal.valueOf(-1000));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Product> expected = Arrays.asList(짜장면, 단무지);
        when(productRepository.findAll()).thenReturn(expected);

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(expected.size()),
                () -> assertThat(actual).containsExactly(ProductResponse.from(짜장면), ProductResponse.from(단무지))
        );
    }
}
