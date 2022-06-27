package kitchenpos.product.application;

import kitchenpos.application.ProductService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    public static final String PRODUCT_NAME01 = "바베큐치킨";
    public static final BigDecimal PRODUCT_PRICE01 = new BigDecimal(30000);

    public static final String PRODUCT_NAME02 = "치즈볼";
    public static final BigDecimal PRODUCT_PRICE02 = new BigDecimal(5000);

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productDao.save(any())).thenReturn(createProduct01());

        // when
        Product created = productService.create(new Product(PRODUCT_NAME01, PRODUCT_PRICE01));

        // then
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격 없이 상품을 생성한다.")
    @Test
    void create_price_null() {
        assertThatThrownBy(() -> {
            productService.create(new Product(PRODUCT_NAME01, null));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 0원 미만으로 상품을 생성한다.")
    @Test
    void create_price_under_zero() {
        assertThatThrownBy(() -> {
            productService.create(new Product(PRODUCT_NAME01, new BigDecimal(-100)));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productService.list()).thenReturn(createProductList());

        // when
        List<Product> list = productService.list();

        // then
        assertThat(list).isNotNull();
    }
    
    public static Product createProduct01() {
        return new Product(1L, PRODUCT_NAME01, PRODUCT_PRICE01);
    }

    public static Product createProduct02() {
        return new Product(2L, PRODUCT_NAME02, PRODUCT_PRICE02);
    }

    public static List<Product> createProductList() {
        return Arrays.asList(createProduct01(), createProduct02());
    }
}
