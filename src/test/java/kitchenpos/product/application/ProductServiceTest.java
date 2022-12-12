package kitchenpos.product.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 서비스")
class ProductServiceTest {

    @DisplayName("상품 생성")
    @Test
    void create() {

    }

    @DisplayName("상품 생성 / 가격을 필수로 갖는다.")
    @Test
    void create_fail_priceNull() {

    }

    @DisplayName("상품 생성 / 가격은 0원보다 작을 수 없다.")
    @Test
    void create_fail_minimumPrice() {
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
