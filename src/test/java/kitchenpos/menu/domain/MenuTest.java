package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class MenuTest {

    @Test
    @DisplayName("메뉴 그룹 없이는 메뉴를 만들 수 없다.")
    void createMenuNonExistMenuGroup() {
        BigDecimal price = new BigDecimal(16000);
        assertThatThrownBy(() -> Menu.of("후라이드치킨", price, null))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }
}
