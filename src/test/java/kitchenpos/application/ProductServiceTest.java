package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.ProductRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void 상품을_등록할_수_있다() {
        ProductRequest 후라이드치킨_요청 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
        Product 후라이드치킨_생성 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        given(productRepository.save(any())).willReturn(후라이드치킨_생성);

        Product product = productService.create(후라이드치킨_요청);

        assertThat(product).isEqualTo(후라이드치킨_생성);
    }

    @Test
    void 상품_가격은_0원_이상_이어야_한다() {
        ProductRequest 가격이_0원_미만인_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(-1));

        ThrowingCallable 가격이_0원_미만인_상품_등록 = () -> productService.create(가격이_0원_미만인_상품);

        assertThatIllegalArgumentException().isThrownBy(가격이_0원_미만인_상품_등록);
    }

    @Test
    void 상품_가격이_null_이면_오류발생() {
        ProductRequest 가격이_null_인_상품 = new ProductRequest(1L, "후라이드치킨", null);

        ThrowingCallable 가격이_null_인_상품_등록 = () -> productService.create(가격이_null_인_상품);

        assertThatIllegalArgumentException().isThrownBy(가격이_null_인_상품_등록);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        given(productRepository.findAll()).willReturn(Collections.singletonList(후라이드치킨));

        List<Product> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(products).containsExactly(후라이드치킨)
        );
    }
}
