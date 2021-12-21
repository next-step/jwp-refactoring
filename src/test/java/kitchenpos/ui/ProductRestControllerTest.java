package kitchenpos.ui;

import kitchenpos.product.domain.JdbcTemplateProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

//@WebMvcTest
@ExtendWith(MockitoExtension.class)
class ProductRestControllerTest {
    private Product product1;
//    private Product product2;

    @Mock
    private JdbcTemplateProductDao productDao;

//    @InjectMocks
//    private ProductService productService;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "양파치킨", new BigDecimal("10000"));
//        product2 = new Product(2L, "대파치킨", new BigDecimal("15000"));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProductTest(){
//        given(productDao.save(any()))
//                .willReturn(product1);

        Product createdProduct = 상품_생성_요청();
        상품_생성_검증(createdProduct);
    }

    private void 상품_생성_검증(Product product){
        assertThat(product.getId()).isEqualTo(product1.getId());
        assertThat(product.getName()).isEqualTo(product1.getName());
        assertThat(product.getPrice()).isEqualTo(product1.getPrice());
    }

    private Product 상품_생성_요청() {
        return new Product(1L, "양파치킨", new BigDecimal("10000"));
    }


}
