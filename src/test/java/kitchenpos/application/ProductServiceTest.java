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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품")
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        product1 = generateProduct(1L, "product1", new BigDecimal(1000));
        product2 = generateProduct(2L, "product2", new BigDecimal(1500));
        product3 = generateProduct(3L, "product3", new BigDecimal(1300));
    }

    @Test
    @DisplayName("전체 상품을 조회할 수 있다.")
    void projectTest1() {
        given(productDao.findAll()).willReturn(Arrays.asList(product1, product2, product3));

        List<Product> products = productService.list();
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("새로운 상품을 추가할 수 있다.")
    void projectTest2() {
        given(productDao.save(any(Product.class))).willReturn(product1);

        Product product = productService.create(product1);
        assertThat(product.getName()).isEqualTo(product1.getName());
    }

    @Test
    @DisplayName("상품 가격은 필수값이며, 음수여서는 안된다.")
    void projectTest3() {
        Product nullPriceProduct = generateProduct(4L, "nullPriceProduct", null);
        Product negativePriceProduct = generateProduct(4L, "negativePriceProduct", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(nullPriceProduct)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(negativePriceProduct)).isInstanceOf(IllegalArgumentException.class);
    }

    public static Product generateProduct(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

}