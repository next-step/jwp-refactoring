package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    public static final Product 불고기버거 = new Product();
    public static final Product 새우버거 = new Product();

    static {
        불고기버거.setId(1L);
        불고기버거.setName("불고기버거");
        불고기버거.setPrice(BigDecimal.valueOf(1000.0));

        새우버거.setId(2L);
        새우버거.setName("새우버거");
        새우버거.setPrice(BigDecimal.valueOf(5000.0));
    }

    @Mock
    ProductDao productDao;
    @InjectMocks
    ProductService productService;

    @Test
    void create() {
        // given
        given(productDao.save(any()))
                .willReturn(new Product());
        // when
        final Product 제품_생성 = productService.create(불고기버거);
        // then
        assertThat(제품_생성).isInstanceOf(Product.class);
    }

    @Test
    void list() {
        // given
        given(productDao.findAll())
                .willReturn(Arrays.asList(불고기버거, 새우버거));
        // when
        final List<Product> list = productService.list();
        // then
        assertThat(list).hasSize(2);
    }
}
