package kitchenpos.product.application;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private final Product 참치김밥 = new Product(1L, "참치김밥", new BigDecimal(3000));
    private final Product 치즈김밥 = new Product(2L, "치즈김밥", new BigDecimal(2500));

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품등록 테스트")
    @Test
    void createProductTest() {
        when(productDao.save(참치김밥)).thenReturn(참치김밥);

        Product productResult = productService.create(참치김밥);

        assertThat(productResult).isEqualTo(참치김밥);
    }

    @DisplayName("상품목록 조회 테스트")
    @Test
    void retrieveProductListTest() {
        //given
        when(productDao.findAll()).thenReturn(Arrays.asList(참치김밥, 치즈김밥));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).contains(참치김밥, 치즈김밥);
    }

    @DisplayName("상품등록 가격 데이터가 없을 경우 오류 테스트")
    @Test
    void createProductPriceNullExceptionTest() {
        assertThatThrownBy(() -> productService.create(new Product(3L, "멸추김밥", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품등록 가격 데이터가 0이하인 경우 오류 테스트")
    @ParameterizedTest
    @ValueSource(longs = {-1, -10})
    void createProductPriceUnderZeroExceptionTest(Long input) {
        assertThatThrownBy(() -> productService.create(new Product(4L, "멸추김밥", new BigDecimal(input))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품중복등록 오류 테스트")
    @Test
    void createProductNameDuplicatedExceptionTest() {
        //given
        when(productService.create(참치김밥)).thenReturn(참치김밥);
        Product 이름중복참치김밥 = new Product(11L, "참치김밥", new BigDecimal(3000));

        //when
        //then
        assertThatThrownBy(() -> productService.create(이름중복참치김밥))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
