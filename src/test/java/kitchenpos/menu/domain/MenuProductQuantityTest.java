package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 수량 관련 Domain 단위 테스트")
class MenuProductQuantityTest {

    @DisplayName("메뉴 상품 수량은 1개 이상이어야 한다.")
    @Test
    void validate() {

        //then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuProductQuantity(-100));
        assertThatIllegalArgumentException()
                .isThrownBy(()-> new MenuProductQuantity(null));
    }

}
