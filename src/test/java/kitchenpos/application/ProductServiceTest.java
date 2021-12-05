package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * - 상품을 등록할 수 있다
 * - 상품의 가격이 올바르지 않으면 등록할 수 없다
 *     - 상품의 가격은 0 원 이상이어야 한다
 * - 상품의 목록을 조회할 수 있다
 */
class ProductServiceTest {

    @Test
    void create_상품을_등록할_수_있다() {
        // given
        ProductDao productDao = mock(ProductDao.class);
        ProductService productService = new ProductService(productDao);
        Product product = new Product();
        product.setId(1L);
        product.setName("강정치킨");
        product.setPrice(new BigDecimal(17_000));
        given(productDao.save(any(Product.class))).willReturn(product);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getName()).isEqualTo("강정치킨"),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal(17_000))
        );
    }
}