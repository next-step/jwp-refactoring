package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Product;

public class MenuTest {
    
    @DisplayName("메뉴 가격은 0원 이상이어야한다")
    @Test
    void 메뉴_가격_0원_이상() {
        // given, when, then
        assertThatThrownBy(() -> {
            Menu.of("치킨", -7000L, MenuGroup.from("메뉴그룹"));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("가격은 0원 이상이어야 합니다");
    }
}
