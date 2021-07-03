package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductDao productDao;

    Product 후라이드치킨;
    Product 양념치킨;

    ProductService productService;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product( 1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));

        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록")
    @Test
    void 상품을_등록() {
        //given
        Product product = new Product("후라이드치킨", BigDecimal.valueOf(18000L));
        when(productDao.save(product)).thenReturn(후라이드치킨);

        //when
        Product savedProduct = productService.create(product);

        //then
        assertThat(savedProduct.getId()).isEqualTo(후라이드치킨.getId());
    }

    @DisplayName("상품_목록을_불러옴")
    @Test
    void 상품_목록을_부러옴() {
        //given
        when(productDao.findAll()).thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        //when
        List<Product> list = productService.list();

        //then
        assertThat(list).hasSize(2);
        assertThat(list).containsExactly(후라이드치킨, 양념치킨);
    }
}
