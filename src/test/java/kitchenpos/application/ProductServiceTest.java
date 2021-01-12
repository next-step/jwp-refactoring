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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품에 대한 비즈니스 로직")
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
        product.setName("강정치킨");
        product.setPrice(new BigDecimal(17000));
    }

    @DisplayName("상품을 생성할수 있다.")
    @Test
    void create() {
        // given
        when(productDao.save(product)).thenReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isEqualTo(product.getId());
        assertThat(actual.getName()).isEqualTo(product.getName());
        assertThat(actual.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("가격은 null이 아니고 0원 이상이어야 한다.")
    @Test
    void priceRange() {
        // given
        product.setPrice(null);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
        product.setPrice(new BigDecimal(-1));
        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
    }

    @DisplayName("메뉴의 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<Product> list = productService.list();

        // then
        assertThat(list.get(0).getId()).isEqualTo(product.getId());
        assertThat(list.get(0).getName()).isEqualTo(product.getName());
        assertThat(list.get(0).getPrice()).isEqualTo(product.getPrice());
    }

}
