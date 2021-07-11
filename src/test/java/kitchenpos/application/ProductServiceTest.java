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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("상품 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("고추 치킨");
    }

    @Test
    void 상품_등록() {
        product.setPrice(new BigDecimal(17000));

        when(productDao.save(product)).thenReturn(product);

        Product expected = 상품_등록(product);

        assertThat(expected.getId()).isEqualTo(product.getId());
        assertThat(expected.getName()).isEqualTo(product.getName());
        assertThat(expected.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    void 등록된_상품_목록_조회() {
        when(productDao.findAll()).thenReturn(Arrays.asList(product));
        List<Product> products = productService.list();
        assertThat(products.size()).isEqualTo(1);
        Product expected = products.get(0);
        assertThat(expected.getId()).isEqualTo(product.getId());
        assertThat(expected.getName()).isEqualTo(product.getName());
        assertThat(expected.getPrice()).isEqualTo(product.getPrice());
    }

    private Product 상품_등록(Product product) {
        return productService.create(product);
    }
}