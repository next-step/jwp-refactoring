package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드;

    @BeforeEach
    void setUp() {
        this.후라이드 = ProductFixture.후라이드;
    }

    @Test
    void 상품_등록시_등록에_성공하고_상품_정보를_반환한다() {
        // given
        Product 상품_등록_요청 = new Product("후라이드", new BigDecimal(12_000));
        given(productDao.save(any())).willReturn(상품_등록_요청);

        // when
        Product 신규_상품 = productService.create(상품_등록_요청);

        // then
        상품_등록됨(신규_상품, 상품_등록_요청.getName(), 상품_등록_요청.getPrice());
    }

    @Test
    void 상품_등록시_상품_가격이_누락된경우_예외처리되어_등록에_실패한다() {
        // given
        Product 가격_누락된_상품_등록_요청 = new Product("후라이드", null);

        // when
        assertThatThrownBy(() -> productService.create(가격_누락된_상품_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        상품_등록실패(가격_누락된_상품_등록_요청);
    }

    @Test
    void 상품_등록시_상품_가격이_0원_미만인경우_예외처리되어_등록에_실패한다() {
        // given
        Product 잘못된_가격의_상품_등록_요청 = new Product("페퍼로니", BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(잘못된_가격의_상품_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        상품_등록실패(잘못된_가격의_상품_등록_요청);
    }

    @Test
    void 상품_목록_조회시_등록된_상품_목록을_반환한다() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(후라이드));

        // when
        List<Product> 상품_목록 = productService.list();

        // then
        상품_목록_조회됨(상품_목록, 후라이드);
    }

    private void 상품_등록됨(Product product, String name, BigDecimal price) {
        then(productDao).should(times(1)).save(any());
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getPrice().floatValue()).isEqualTo(price.floatValue())
        );
    }

    private void 상품_등록실패(Product product) {
        then(productDao).should(times(0)).save(any());
    }

    private void 상품_목록_조회됨(List<Product> products, Product... expectedProducts) {
        then(productDao).should(times(1)).findAll();
        assertThat(products).hasSize(expectedProducts.length);
    }
}
