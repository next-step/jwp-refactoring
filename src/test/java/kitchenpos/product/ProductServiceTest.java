package kitchenpos.product;

import kitchenpos.common.ServiceTest;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.*;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService service;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ProductRequest product = new ProductRequest("강정치킨", BigDecimal.TEN);
        ProductResponse response = service.create(product);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo("강정치킨"),
                () -> assertThat(response.getPrice()).isEqualTo(BigDecimal.TEN)
        );
    }
}
