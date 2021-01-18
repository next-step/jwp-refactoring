package kitchenpos.application;

import kitchenpos.application.ProductService;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("강정치킨", new BigDecimal(17000));
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createProduct() {
        // given
        when(productDao.save(product)).thenReturn(product);

        // when
        Product createdProduct = productService.create(product);

        // then
        assertThat(createdProduct.getId()).isEqualTo(product.getId());
        assertThat(createdProduct.getName()).isEqualTo(product.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("상품 생성시 가격은 필수 정보이다.")
    @Test
    void createProductPriceException1() {
        // given
        product.setPrice(null);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(product);
        });
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void createProductPriceException2() {
        // given
        product.setPrice(new BigDecimal(-100));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            productService.create(product);
        });
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findProducts() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(product));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.get(0).getId()).isEqualTo(product.getId());
        assertThat(products.get(0).getName()).isEqualTo(product.getName());
        assertThat(products.get(0).getPrice()).isEqualTo(product.getPrice());
    }

}
