package kitchenpos.application;

import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductDao productDao;

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
        Product 상품_등록_요청_데이터 = new Product();
        상품_등록_요청_데이터.setName("강정치킨");
        상품_등록_요청_데이터.setPrice(BigDecimal.valueOf(17_000));

        given(productDao.save(any(Product.class))).willReturn(강정치킨);

        // when
        Product 등록된_상품 = productService.create(상품_등록_요청_데이터);

        // then
        assertThat(등록된_상품).isEqualTo(강정치킨);
    }

    @DisplayName("상품 목록 확인")
    @Test
    void 상품_목록_확인() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(강정치킨, 후라이드치킨));

        // when
        List<Product> 상품_목록 = productService.list();

        // then
        assertThat(상품_목록).containsExactly(강정치킨, 후라이드치킨);
    }

    @DisplayName("상품 등록 실패 테스트")
    @Nested
    class CreateFailTest {
        @DisplayName("가격이 존재하지 않음")
        @Test
        void 가격이_존재하지_않음() {
            // given
            Product 상품_등록_요청_데이터 = new Product();
            상품_등록_요청_데이터.setName("강정치킨");
            상품_등록_요청_데이터.setPrice(null);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 이상이 아님")
        @Test
        void 가격이_0원_이상이_아님() {
            // given
            Product 상품_등록_요청_데이터 = new Product();
            상품_등록_요청_데이터.setName("강정치킨");
            상품_등록_요청_데이터.setPrice(BigDecimal.valueOf(-1));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> productService.create(상품_등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
