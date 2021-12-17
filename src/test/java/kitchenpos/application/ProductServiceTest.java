package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관리 기능")
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
    @DisplayName(" `상품`을 등록할 수 있다.")
    void 상품_등록() {
        // given
        Product 치킨 = 상품생성("치킨", 15000);
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
        Product 치킨 = 상품생성("치킨", -15000);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(치킨);
        });
    }


    @Test
    @DisplayName("`상품`목록을 조회 할 수 있다.")
    void 상품_목록_조회() {
        // given
        Product 치킨 = 상품이_등록되어_있음("치킨", 15000);
        when(productDao.findAll()).thenReturn(Collections.singletonList(치킨));

        // when
        List<Product> 상품목록 = productService.list();

        //
        assertThat(상품목록).isNotEmpty();
    }

    private void 상품이_등록됨(Product 상품) {
        assertThat(상품).isNotNull();
    }

    private Product 상품생성(String 상품이름, int 상품가격) {
        Product product = new Product();
        product.setName(상품이름);
        product.setPrice(BigDecimal.valueOf(상품가격));
        return product;
    }

    private Product 상품이_등록되어_있음(String 상품이름, int 상품가격) {
        Product 치킨 = 상품생성(상품이름, 상품가격);
        productService.create(치킨);
        return 치킨;
    }
}