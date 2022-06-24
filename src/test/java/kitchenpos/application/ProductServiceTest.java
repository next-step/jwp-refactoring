package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    ProductService productService;

    private Product 치킨;
    private Product 피자;

    @BeforeEach
    void setUp() {
        치킨 = new Product( 1L, "치킨", BigDecimal.valueOf(15000L));
        피자 = new Product(2L, "피자", BigDecimal.valueOf(20000L));
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void 상품_등록(){
        //given
        Product product = new Product("치킨", BigDecimal.valueOf(15000L));
        given(productDao.save(product)).willReturn(치킨);

        //when
        Product savedProduct = productService.create(product);

        //then
        assertThat(savedProduct.getId()).isEqualTo(치킨.getId());
    }


    @DisplayName("상품의 가격은 0 이상이다")
    @Test
    void 상품_가격_검증(){
        //given
        Product invalidProduct = new Product("치킨", BigDecimal.valueOf(-15000L));

        //then
       assertThrows(IllegalArgumentException.class, () -> productService.create(invalidProduct));
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void 상품_목록_조회() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(치킨, 피자));

        //when
        List<Product> list = productService.list();

        //then
        assertThat(list).containsExactly(치킨, 피자);
    }
}