package kitchenpos.product.service;

import kitchenpos.IntegrationTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductServiceJpaTest extends IntegrationTest {

    @Autowired
    private ProductServiceJpa productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {

        ProductResponse productResponse = productService.create(new ProductRequest("치킨", 17000));

        assertThat(productResponse.getName()).isEqualTo("치킨");
        assertThat(productResponse.getPrice()).isEqualTo(17000);
    }

    @DisplayName("상품 가격은 0원 이상이거나 null이 아니어야 한다.")
    @Test
    void isNotCollectProductPrice() {
        assertAll(
                () -> assertThatThrownBy(() -> productService.create(new ProductRequest("치킨", -1)))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> productService.create(new ProductRequest("치킨", new Integer(null))))
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void getProductFindAll() {
        List<ProductResponse> productResponses = productService.list();

        assertThat(productResponses)
                .extracting("name")
                .containsExactly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }
}