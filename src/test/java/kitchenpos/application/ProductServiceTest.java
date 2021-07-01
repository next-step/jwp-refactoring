package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

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
        후라이드치킨 = new Product(1L, "후라이드치킨", BigDecimal.valueOf(18000L));
        양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(19000L));

        productService = new ProductService(productDao);
    }

    @Test
    void create() {
        //given
        when(productDao.save(후라이드치킨)).thenReturn(후라이드치킨);

        //when
        Product savedProduct = productService.create(후라이드치킨);

        //then
        assertThat(savedProduct.getId()).isEqualTo(후라이드치킨.getId());
    }

    @Test
    void list() {
        //given
        when(productDao.findAll()).thenReturn(Arrays.asList(후라이드치킨, 양념치킨));

        //when, then
        assertThat(productService.list()).hasSize(2);
    }
}
