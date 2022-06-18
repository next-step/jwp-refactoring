package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관련 Service 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp(){
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        //given
        Product product = new Product(BigDecimal.valueOf(19000));
        when(productDao.save(any())).thenReturn(product);

        //when
        Product result = productService.create(product);

        //then
        assertThat(result).isEqualTo(product);
    }

    @DisplayName("상품 가격이 null 이거나 0원 미만이면 등록 할 수 없다.")
    @Test
    void create_price_null_or_less_then_zero() {
        //given
        Product product1 = new Product(BigDecimal.valueOf(-1));
        Product product2 = new Product();

        //when then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(product1)),
                () -> assertThatIllegalArgumentException().isThrownBy(()-> productService.create(product2))
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //given
        Product product1 = new Product(BigDecimal.valueOf(19000));
        Product product2 = new Product(BigDecimal.valueOf(15000));
        Product product3 = new Product(BigDecimal.valueOf(13000));
        when(productDao.findAll()).thenReturn(Arrays.asList(product1, product2, product3));

        //when
        List<Product> result = productService.list();

        //then
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(product1, product2, product3));
    }
}
