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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product 매콤치킨;
    private Product 치즈볼;
    private Product 사이다;

    @BeforeEach
    void setUp() {
        매콤치킨 = createProduct(1L, "매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = createProduct(2L, "치즈볼", BigDecimal.valueOf(2000));
        사이다 = createProduct(3L, "사이다", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        when(productDao.save(any())).thenReturn(매콤치킨);

        Product product = productService.create(매콤치킨);

        verify(productDao, times(1)).save(any(Product.class));
        assertThat(product.getName()).isEqualTo(매콤치킨.getName());
    }

    @Test
    @DisplayName("상품의 가격이 null인 경우 예외가 발생한다.")
    void validatePriceNull() {
        Product product = createProduct(4L, "통통치킨", null);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @Test
    @DisplayName("상품의 가격이 0원 미만인 경우 예외가 발생한다.")
    void validateMinPrice() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        Product product = createProduct(4L, "통통치킨", invalidPrice);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        when(productDao.findAll()).thenReturn(Arrays.asList(매콤치킨, 치즈볼, 사이다));

        List<Product> products = productService.list();

        verify(productDao, times(1)).findAll();
        assertThat(products).hasSize(3);
    }

    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}