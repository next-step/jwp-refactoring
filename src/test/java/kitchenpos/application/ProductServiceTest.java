package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
    @MockBean
    private ProductDao productDao;
    private ProductService productService;
    private Product product;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(17_000));
        product.setName("양념치킨");
        when(productDao.save(any(Product.class))).thenReturn(product);
        when(productDao.findAll()).thenReturn(Collections.singletonList(product));
    }

    @DisplayName("제품을 등록한다")
    @Test
    void createProduct() {
        Product savedProduct = productService.create(product);
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("가격이 부적합한 제품을 등록한다")
    @ParameterizedTest
    @MethodSource
    void createProductWithIllegalArguments(BigDecimal price) {
        product.setPrice(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    private static Stream<Arguments> createProductWithIllegalArguments() {
        return Stream.of(
                Arguments.of((Object)null),
                Arguments.of(new BigDecimal(-1))
        );
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<Product> products = productService.list();
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0)).isEqualTo(product);
    }
}
