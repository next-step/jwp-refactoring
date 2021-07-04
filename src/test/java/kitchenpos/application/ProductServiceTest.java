package kitchenpos.application;

import static kitchenpos.utils.DataInitializer.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.utils.DataInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("제품 서비스")
class ProductServiceTest {

    ProductService productService;

    @Mock
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("제품을 생성한다")
    void create() {
        // given
        when(productDao.save(치킨)).thenReturn(치킨);

        // when
        Product savedProduct = productService.create(치킨);

        // then
        assertThat(savedProduct.getId()).isEqualTo(치킨.getId());
    }

    @Test
    @DisplayName("제품 생성 실패(가격이 음수이거나 null)")
    void create_failed() {
        // given
        Product noPriceProduct = new Product(1L, "엽기떡볶이", null);

        // then
        assertThatThrownBy(() -> productService.create(noPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제품 목록을 가져온다")
    void list() {
        // given
        List<Product> products = Arrays.asList(치킨, 피자, 소주, 맥주);
        when(productDao.findAll()).thenReturn(products);

        // when
        List<Product> allProducts = productService.list();
        assertThat(allProducts.size()).isEqualTo(4);
        assertThat(allProducts).containsExactly(치킨, 피자, 소주, 맥주);
    }
}
