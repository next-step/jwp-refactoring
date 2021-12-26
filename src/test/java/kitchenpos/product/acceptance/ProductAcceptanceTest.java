package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
public class ProductAcceptanceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    private Product product;

    @BeforeEach
    void setUp() {
        product = 상품_정보(1L, "제육볶음", 8900);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // given
        given(productDao.save(any(Product.class))).willReturn(product);
        Product expectedProduct = productService.create(product);

        // then
        assertThat(expectedProduct).isNotNull();
        assertThat(expectedProduct.getName()).isEqualTo("제육볶음");
        assertThat(expectedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(8900));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list(){
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(product));
        List<Product> products = productService.list();

        // then
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(1);
    }

    private Product 상품_정보(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}
