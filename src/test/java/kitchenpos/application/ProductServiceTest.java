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
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    public Product 상품1;
    public Product 상품2;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        상품1 = new Product();
        상품1.setId(1L);
        상품1.setName("반반콤보");
        상품1.setPrice(new BigDecimal(18000));

        상품2 = new Product();
        상품2.setId(2L);
        상품2.setName("허니콤보");
        상품2.setPrice(new BigDecimal(18000));

    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        given(productDao.save(상품1)).willReturn(상품1);

        Product createdProduct = productService.create(this.상품1);

        assertThat(createdProduct.getId()).isEqualTo(this.상품1.getId());
        assertThat(createdProduct.getName()).isEqualTo(this.상품1.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(this.상품1.getPrice());
    }

    @DisplayName("상품의 가격이 0원 이상일때 등록이 가능하다.")
    @Test
    void checkProductPrice() {
        상품1.setPrice(new BigDecimal(-10));

        assertThatThrownBy(
                () -> productService.create(this.상품1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void searchProductList() {
        given(productDao.findAll()).willReturn(Arrays.asList(상품1, 상품2));

        List<Product> products = productService.list();

        assertThat(products).containsExactly(상품1, 상품2);
        assertThat(products.get(0).getName()).isEqualTo(상품1.getName());
        assertThat(products.get(1).getName()).isEqualTo(상품2.getName());
    }
}
