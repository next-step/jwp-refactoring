package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ProductRequest given = new ProductRequest("짜장면", 6000);
        when(productRepository.save(any(Product.class))).thenReturn(given.toEntity());

        // when
        ProductResponse actual = productService.create(given);

        // then
        assertThat(actual.getName()).isEqualTo(given.getName());
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
