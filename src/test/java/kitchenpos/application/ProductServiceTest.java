package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends ServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    void 상품_추가(){
        String name = "상품1";
        int price = 1000;
        Product product = ProductFixtureFactory.createProduct(name,price);
        Product savedProduct = productService.create(product);
        assertThat(savedProduct.getName()).isEqualTo(name);
        assertThat(savedProduct.getPrice().intValue()).isEqualTo(price);
    }

    @Test
    void 상품_추가_실패(){
        String name = "상품1";
        int price = -1000;
        Product product = ProductFixtureFactory.createProduct(name,price);
        assertThatIllegalArgumentException().isThrownBy(()->{
            productService.create(product);
        });
    }
}
