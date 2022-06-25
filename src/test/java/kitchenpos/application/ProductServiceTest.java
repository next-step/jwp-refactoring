package kitchenpos.application;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends ServiceTest {
    @Autowired
    private ServiceTestHelper serviceTestHelper;

    @Autowired
    private ProductService productService;

    @Test
    void 상품_추가(){
        String name = "상품1";
        int price = 1000;
        Product savedProduct = serviceTestHelper.상품_생성됨(name,price);
        assertThat(savedProduct.getName()).isEqualTo(name);
        assertThat(savedProduct.getPrice().intValue()).isEqualTo(price);
    }

    @Test
    void 상품_추가_실패(){
        String name = "상품1";
        int price = -1000;
        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.상품_생성됨(name,price);
        });
    }

    @Test
    void 상품목록_조회(){
        serviceTestHelper.상품_생성됨("상품1",1000);
        serviceTestHelper.상품_생성됨("상품2",2000);
        List<Product> products = productService.list();

        assertThat(products).hasSize(2);
    }
}
