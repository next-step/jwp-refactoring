package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuTest {

    @DisplayName("메뉴 생성")
    @Test
    void createMenu() {

        //given
        final String menuName = "후라이드세트";
        final BigDecimal menuPrice = new BigDecimal("24000");

        //when
        Menu menu = Menu.create(menuName, menuPrice);

        //then
        assertThat(menu).isNotNull();
    }


}
