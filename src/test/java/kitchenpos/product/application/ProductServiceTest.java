package kitchenpos.product.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static kitchenpos.common.Price.PRICE_NOT_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 서비스")
class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성")
    @Test
    void create() {

    }

    @DisplayName("상품 생성 / 가격을 필수로 갖는다.")
    @Test
    void create_fail_priceNull() {
        assertThatThrownBy(() -> productService.create(new ProductCreateRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 생성 / 가격은 0원보다 작을 수 없다.")
    @Test
    void create_fail_minimumPrice() {
//        assertThatThrownBy(() -> productService.create(new ProductCreateRequest()))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 생성 / 이름을 필수로 갖는다.")
    @Test
    void create_fail_name() {
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {

    }
}
