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

import static kitchenpos.factory.fixture.ProductFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        Product product = createProduct(1L, "닭", BigDecimal.valueOf(1000L));
        given(productDao.save(any(Product.class))).willReturn(product);

        Product result = productService.create(createProduct("닭", BigDecimal.valueOf(1000L)));

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("닭");
    }

    @DisplayName("가격이 0원 이상이 아니면, 상품을 생성할 수 없다.")
    @Test
    void create_invalid_price() {
        assertThatThrownBy(() -> productService.create(createProduct("닭", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        Product firstProduct = createProduct(1L, "닭", BigDecimal.valueOf(1000L));
        Product secondProduct = createProduct(2L, "콜라", BigDecimal.valueOf(500L));
        given(productDao.findAll()).willReturn(Arrays.asList(firstProduct, secondProduct));

        List<Product> products = productService.list();

        assertThat(products).hasSize(2);
    }
}
