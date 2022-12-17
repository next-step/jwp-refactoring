package kitchenpos.menu.domain;

import static kitchenpos.menu.MenuFixture.더블강정치킨;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("가격정보가 없거나 0원이하면 안됩니다.")
    void validateMenuPrice() {
        Assertions.assertThatThrownBy(
            () -> new Menu(더블강정치킨.getId(), 더블강정치킨.getName(), null, 더블강정치킨.getMenuGroupId(),
                더블강정치킨.getMenuProducts()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("가격정보가 없거나 0원이하면 안됩니다.");
    }
}