package kitchenpos.product.application;

import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    Product 후라이드치킨;
    Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(16000));
        양념치킨 = ProductFixture.of(2L, "양념치킨", BigDecimal.valueOf(17000));
    }

    @Test
    void 상품_등록() {
        // given
        given(productDao.save(any())).willReturn(후라이드치킨);

        // when
        Product actual = productService.create(후라이드치킨);

        // then
        Assertions.assertThat(actual).isEqualTo(후라이드치킨);
    }

    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        Product 마이너스치킨 = ProductFixture.of(3L, "마이너스치킨", BigDecimal.valueOf(-1));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () ->  productService.create(마이너스치킨);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 상품_조회() {
        // given
        List<Product> products = Arrays.asList(후라이드치킨, 양념치킨);
        given(productDao.findAll()).willReturn(products);

        // when
        List<Product> actual = productService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).containsExactlyElementsOf(Arrays.asList(후라이드치킨, 양념치킨));
        });
    }
}
