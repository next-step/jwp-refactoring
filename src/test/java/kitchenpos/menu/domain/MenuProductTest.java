package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void create() {

    }

    @DisplayName("메뉴 상품을 생성한다. / 메뉴를 필수로 갖는다.")
    @Test
    void create_fail_menu() {
    }

    @DisplayName("메뉴 상품을 생성한다. / 상품을 필수로 갖는다.")
    @Test
    void create_fail_product() {
    }

    @DisplayName("메뉴 상품을 생성한다. / 갯수를 필수로 갖는다.")
    @Test
    void create_fail_quantity() {
    }
}
