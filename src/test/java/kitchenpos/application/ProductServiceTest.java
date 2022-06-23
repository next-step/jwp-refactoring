package kitchenpos.application;

import java.util.List;
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

    @Test
    void 상품목록_조회(){
        productService.create(ProductFixtureFactory.createProduct("상품1",1000));
        productService.create(ProductFixtureFactory.createProduct("상품2",2000));
        List<Product> products = productService.list();
        assertThat(products).hasSize(2);
    }
}
