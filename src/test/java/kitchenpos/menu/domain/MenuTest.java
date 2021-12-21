package kitchenpos.menu.domain;

import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 단위테스트")
class MenuTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void saveMenuTest() {
        Menu menu = new Menu(1L, "스페셜치킨", new BigDecimal("10000"), 1L);
        checkValidMenu(menu);
    }

    @Test
    @DisplayName("잘못된 가격의 메뉴를 등록하면 에러 처리")
    void saveWrongPriceMenuTest() {
        Assertions.assertThatThrownBy(() -> {
                    new Menu(1L, "스페셜치킨", new BigDecimal(-10000), 1L);
                }).isInstanceOf(InputMenuDataException.class)
                .hasMessageContaining(InputMenuDataErrorCode.IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO.errorMessage());
    }

    private void checkValidMenu(Menu menu) {
        assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("스페셜치킨"),
                () -> assertThat(menu.getPrice()).isEqualTo(new BigDecimal("10000")),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L)
        );
    }
}
