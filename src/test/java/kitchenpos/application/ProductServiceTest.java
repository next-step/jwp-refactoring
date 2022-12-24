package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        Product product = new Product(1L, "name", BigDecimal.valueOf(1000));
        when(productDao.save(product)).thenReturn(product);

        Product savedProduct = productService.create(product);

        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    void 상품의_가격이_올바르지_않으면_등록할_수_없다() {
        Product product = new Product(1L, "name", BigDecimal.valueOf(-1));

        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
    }

    @Test
    void 상품의_목록을_조회할_수_있다() {
        Product product = new Product(1L, "name", BigDecimal.valueOf(-1));
        when(productDao.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.list();

        assertThat(products).hasSize(1);
        assertThat(products).contains(product);
    }
}
