package kitchenpos.menu.domain;

import kitchenpos.common.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    private String 메뉴이름 = "메뉴이름";
    private MenuGroup 메뉴그룹 = new MenuGroup("메뉴그룹");
    private List<MenuProduct> 메뉴상품목록 = new ArrayList<>();

    @DisplayName("메뉴 등록시, 가격정보가 입력되지 않으면 예외발생")
    @Test
    void 메뉴등록시_가격정보_없는_경우_예외발생(){
        BigDecimal 비어있는_가격정보 = null;

        assertThatThrownBy(() -> new Menu(메뉴이름, 비어있는_가격정보, 메뉴그룹, 메뉴상품목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENU_PRICE_REQUIRED.showText());
    }

    @DisplayName("메뉴 등록시, 가격정보가 0원 미만인 경우 예외발생")
    @Test
    void 메뉴등록시_가격정보_0원_미만인_경우_예외발생(){
        BigDecimal 음수인_가격정보 = BigDecimal.valueOf(-1000);

        assertThatThrownBy(() -> new Menu(메뉴이름, 음수인_가격정보, 메뉴그룹, 메뉴상품목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENU_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
    }
}
