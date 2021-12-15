package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 저장한다")
    @Test
    void testCreate() {
        // given
        Product 볶음짜장면 = new Product("볶음짜장면", 8000L);
        Product expectedProduct = new Product(1L, "볶음짜장면", 8000L);
        given(productDao.save(any())).willReturn(expectedProduct);
        // when
        Product product = productService.create(볶음짜장면);
        // then
        assertThat(product).isEqualTo(expectedProduct);
    }

    @DisplayName("상품 가격은 0원 이상 이어야 한다")
    @Test
    void givenZeroPriceWhenSaveThenThrowException() {
        // given
        Product 볶음짜장면 = new Product("볶음짜장면", -1L);
        // when, then
        assertThatThrownBy(() -> productService.create(볶음짜장면))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 상품을 조회한다")
    @Test
    void testFindAll() {
        // given
        List<Product> expectedProducts = Arrays.asList(new Product("볶음짜장면", 8000L));
        given(productDao.findAll()).willReturn(expectedProducts);
        // when
        List<Product> products = productService.list();
        // then
        assertThat(products).isEqualTo(expectedProducts);
    }
}
