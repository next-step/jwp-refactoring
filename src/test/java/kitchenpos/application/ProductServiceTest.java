package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    void 상품을_등록할_수_있다() {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        given(productDao.save(any())).willReturn(후라이드치킨);

        Product product = productService.create(후라이드치킨);

        assertThat(product).isEqualTo(후라이드치킨);
    }

    @Test
    void 상품_가격은_0원_이상_이어야_한다() {
        Product 가격이_0원_미만인_상품 = new Product(1L, "후라이드치킨", new BigDecimal(-1));

        ThrowingCallable 가격이_0원_미만인_상품_등록 = () -> productService.create(가격이_0원_미만인_상품);

        assertThatIllegalArgumentException().isThrownBy(가격이_0원_미만인_상품_등록);
    }

    @Test
    void 상품_가격이_null_이면_오류발생() {
        Product 가격이_null_인_상품 = new Product(1L, "후라이드치킨", null);

        ThrowingCallable 가격이_null_인_상품_등록 = () -> productService.create(가격이_null_인_상품);

        assertThatIllegalArgumentException().isThrownBy(가격이_null_인_상품_등록);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000.00));
        given(productDao.findAll()).willReturn(Collections.singletonList(후라이드치킨));

        List<Product> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(products).containsExactly(후라이드치킨)
        );
    }
}
