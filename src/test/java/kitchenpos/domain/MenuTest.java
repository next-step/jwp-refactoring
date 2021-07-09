package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {
    @DisplayName("메뉴 생성 예외 - 가격 미입력시")
    @Test
    public void 가격미입력시_메뉴생성_예외() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
        MenuProduct menuProduct = new MenuProduct(1L, null,
                new Product(1L, "", BigDecimal.TEN), 1L);
        
        //when
        //then
        assertThatThrownBy(() -> new Menu(1L, "반반치킨", null, menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 예외 - 가격 음수입력시")
    @Test
    public void 가격음수입력시_메뉴생성_예외() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
        MenuProduct menuProduct = new MenuProduct(1L, null,
                new Product(1L, "", BigDecimal.TEN), 1L);

        //when
        //then
        assertThatThrownBy(() -> new Menu(1L, "반반치킨", BigDecimal.valueOf(-1),
                menuGroup, Arrays.asList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
