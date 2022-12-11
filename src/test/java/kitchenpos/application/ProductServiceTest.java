package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductDao productDao;

    ProductService productService;

    @BeforeEach
    void beforeEach(){
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품 생성 성공 테스트")
    void createTest(){
        // given

        // when
        Product savedProduct = productService.create(new Product("1번 상품", new BigDecimal(1000)));

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품 등록 시, 가격은 null 일 수 없습니다.")
    void createFail1Test(){
        // given

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(new Product("1번 상품", null))
        );

        // then
    }

    @Test
    @DisplayName("상품 등록 시, 가격은 음수 일 수 없습니다.")
    void createFail2Test(){
        // given

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(new Product("1번 상품", new BigDecimal(-1)))
        );

        // then
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void listTest(){
        // given
        Product product1 = productService.create(new Product("1번 상품", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("2번 상품", new BigDecimal(2000)));
        Product product3 = productService.create(new Product("3번 상품", new BigDecimal(3000)));

        // when
        List<Product> products = productService.list();

        // then
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        assertThat(productIds).contains(
                product1.getId(), product2.getId(), product3.getId()
        );
    }
}