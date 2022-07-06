package menu.domain;

import static menu.fixture.MenuGroupFixture.메뉴묶음_데이터_생성;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = 메뉴묶음_데이터_생성(1L, "name");
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(1000);

        //when
        Menu menu = new Menu(name, price, menuGroup, Collections.emptyList());

        //then
        assertAll(
                () -> assertEquals(name, menu.getName()),
                () -> assertEquals(price, menu.getPrice()),
                () -> assertEquals(menuGroup.getId(), menu.getMenuGroup().getId())
        );
    }

}