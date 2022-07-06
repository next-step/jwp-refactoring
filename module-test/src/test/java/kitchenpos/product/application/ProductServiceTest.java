package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productRepository.save(any())).thenReturn(createProduct1());

        // when
        ProductResponse created = productService.create(new ProductRequest(PRODUCT_NAME01, PRODUCT_PRICE01));

        // then
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격 없이 상품을 생성한다.")
    @Test
    void create_price_null() {
        assertThatThrownBy(() -> {
            productService.create(new ProductRequest(PRODUCT_NAME01, null));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 0원 미만으로 상품을 생성한다.")
    @Test
    void create_price_under_zero() {
        assertThatThrownBy(() -> {
            productService.create(new ProductRequest(PRODUCT_NAME01, new BigDecimal(-100)));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productRepository.findAll()).thenReturn(createProductList());

        // when
        List<ProductResponse> list = productService.list();

        // then
        assertThat(list).isNotNull();
    }

    public static Product createProduct1() {
        return new Product(1L, PRODUCT_NAME01, PRODUCT_PRICE01);
    }

    public static Product createProduct2() {
        return new Product(2L, PRODUCT_NAME02, PRODUCT_PRICE02);
    }

    public static List<Product> createProductList() {
        return Arrays.asList(createProduct1(), createProduct2());
    }
}
