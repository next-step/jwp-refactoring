package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("메뉴 생성 시도 시 메뉴 가격이 없을 경우 에러 반환")
    public void createPriceNullException() {
        assertThatThrownBy(() -> new Menu("메뉴명",null, null, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 생성 시도 시 메뉴 가격이 음수 일 경우 에러 반환")
    public void createPriceUnderZeroException() {
        Menu menu = new Menu();

        assertThatThrownBy(() -> menu.setPrice(new Price(BigDecimal.valueOf(-1)))).isInstanceOf(IllegalArgumentException.class);
    }
}
