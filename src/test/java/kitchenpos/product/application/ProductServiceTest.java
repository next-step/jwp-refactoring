package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@DisplayName("상품 서비스 관련")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    ProductService productService;
    @MockBean
    ProductRepository productRepository;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
        // given
        Product given = new Product("짜장면", 6000);

        // when
        productService.create(given);

        // then
        verify(productRepository).save(given);
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        productService.list();

        // then
        verify(productRepository).findAll();
    }
}
