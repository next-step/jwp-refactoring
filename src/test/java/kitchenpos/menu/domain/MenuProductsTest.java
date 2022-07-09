package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴상품은 하나 이상 존재 해야한다.")
    void validate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MenuProducts.from(new ArrayList<>()));
    }

}
