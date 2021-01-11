package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static kitchenpos.util.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        given(productDao.save(후라이드)).willReturn(후라이드);

        Product result = productService.create(후라이드);

        assertThat(result).isEqualTo(후라이드);
    }

    @DisplayName("상품의 가격이 설정되어 있지 않을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException1() {
        assertThatThrownBy(() -> productService.create(product_price_변경(후라이드, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0원보다 작을 경우 상품을 등록할 수 없다.")
    @Test
    void createProductException2() {
        assertThatThrownBy(() -> productService.create(product_price_변경(후라이드, BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
