package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;

@DisplayName("상품 서비스 관련")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    ProductService productService;
    @MockBean
    ProductDao productDao;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create() {
        // given
        Product product = new Product("짜장면", BigDecimal.valueOf(6000));

        // when
        productService.create(product);

        // then
        verify(productDao).save(product);
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다")
    @Test
    void createPriceZero() {
        // given
        Product nullPrice = new Product("짬뽕", null);
        Product minusPrice = new Product("탕수육", BigDecimal.valueOf(-1));

        // when then
        assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> productService.create(nullPrice))
                    .isInstanceOf(IllegalArgumentException.class);
            softAssertions.assertThatThrownBy(() -> productService.create(minusPrice))
                    .isInstanceOf(IllegalArgumentException.class);
        });
    }

    @DisplayName("상품의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        productService.list();

        // then
        verify(productDao).findAll();
    }
}
