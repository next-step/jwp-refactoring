package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void saveProduct() {
        final Product product = new Product(1L, "상품", new BigDecimal("1000"));
        given(productDao.save(any())).willReturn(product);

        final Product actual = productService.create(product);

        assertThat(actual).isEqualTo(product);
    }

    @DisplayName("등록한 상품을 조회한다.")
    @Test
    void findProducts() {
        final Product product1 = new Product(1L, "상품", new BigDecimal("1000"));
        final Product product2 = new Product(2L, "상품2", new BigDecimal("1000"));
        final List<Product> products = Arrays.asList(product1, product2);
        given(productDao.findAll()).willReturn(products);

        final List<Product> actual = productService.list();

        assertThat(actual).hasSize(2);
    }
}
