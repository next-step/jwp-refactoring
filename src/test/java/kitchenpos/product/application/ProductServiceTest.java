package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private Product 강정치킨;
    private Product 후라이드치킨;

    @BeforeEach
    void setup() {
        강정치킨 = ProductFixture.create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        후라이드치킨 = ProductFixture.create(2L, "후라이드치킨", BigDecimal.valueOf(16_000));
    }

    @DisplayName("상품 등록 확인")
    @Test
    void 상품_등록_확인() {
        // given
        ProductRequest 상품_등록_요청_데이터 = ProductRequest.of("강정치킨", BigDecimal.valueOf(17_000));

        doReturn(강정치킨).when(productRepository)
                .save(any(Product.class));


        // when
        ProductResponse 등록된_상품_응답 = productService.create(상품_등록_요청_데이터);

        // then
        assertAll(
                () -> assertThat(등록된_상품_응답.getId()).isNotNull(),
                () -> assertThat(등록된_상품_응답.getName()).isEqualTo(상품_등록_요청_데이터.getName()),
                () -> assertThat(등록된_상품_응답.getPrice()).isEqualByComparingTo(상품_등록_요청_데이터.getPrice())
        );
    }

    @DisplayName("상품 목록 확인")
    @Test
    void 상품_목록_확인() {
        // given
        doReturn(Arrays.asList(강정치킨, 후라이드치킨)).when(productRepository)
                .findAll();

        // when
        List<ProductResponse> 상품_목록 = productService.list();

        // then
        assertAll(
                () -> assertThat(상품_목록).hasSize(2),
                () -> 상품_목록_조회_확인(상품_목록, Arrays.asList(강정치킨, 후라이드치킨))
        );
    }

    private void 상품_목록_조회_확인(List<ProductResponse> 조회된_상품_목록, List<Product> 요청된_상품_목록) {
        List<Long> 조회된_상품_ID_목록 = 조회된_상품_목록.stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        List<Long> 요청된_상품_ID_목록 = 요청된_상품_목록.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(조회된_상품_ID_목록).containsExactlyElementsOf(요청된_상품_ID_목록);
    }

    @DisplayName("상품 등록 실패 테스트")
    @Nested
    class CreateFailTest {
        @DisplayName("가격이 존재하지 않음")
        @Test
        void 가격이_존재하지_않음() {
            // given
            ProductRequest 상품_등록_요청_데이터 = ProductRequest.of("강정치킨", null);


            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 이상이 아님")
        @Test
        void 가격이_0원_이상이_아님() {
            // given
            ProductRequest 상품_등록_요청_데이터 = ProductRequest.of("강정치킨", BigDecimal.valueOf(-1L));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이름이 존재하지 않음")
        @Test
        void 이름이_존재하지_않음() {
            // given
            ProductRequest 상품_등록_요청_데이터 = ProductRequest.of(null, BigDecimal.valueOf(17_000L));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이름이 비어있음")
        @Test
        void 이름이_비어있음() {
            // given
            ProductRequest 상품_등록_요청_데이터 = ProductRequest.of("", BigDecimal.valueOf(17_000L));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
