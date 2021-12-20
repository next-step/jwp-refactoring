package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.상품생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
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
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;
    private Product 치킨;

    @BeforeEach
    void setUp() {
        치킨 = 상품생성(1L, "치킨", 15000);
    }

    @Test
    @DisplayName(" `상품`을 등록할 수 있다.")
    void 상품_등록() {
        // given
        when(productDao.save(any())).thenReturn(치킨);

        // when
        Product 등록된_치킨 = productService.create(치킨);

        // then
        상품이_등록됨(등록된_치킨);
    }


    @Test
    @DisplayName("상품의 가격은 0원 미만(음수)이면 상품을 등록 할 수 없다.")
    void 상품_가격이_음수인경우_실패한다() {
        // given
        치킨.setPrice(BigDecimal.valueOf(-1000));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(치킨);
        });
    }

    @Test
    @DisplayName("`상품`목록을 조회 할 수 있다.")
    void 상품_목록_조회() {
        // given
        when(productDao.findAll()).thenReturn(Collections.singletonList(치킨));

        // when
        List<Product> 상품목록 = productService.list();

        //
        상품목록_조회됨(상품목록);
    }

    private void 상품목록_조회됨(List<Product> 상품목록) {
        assertThat(상품목록).isNotEmpty();
    }

    private void 상품이_등록됨(Product 상품) {
        assertThat(상품).isNotNull();
    }
}
