package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_이름이_존재_해야_한다() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Menu("", BigDecimal.valueOf(1000))),
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Menu("", BigDecimal.valueOf(1000)))
        );
    }

    @Test
    void 메뉴_가격은_0이상이어야_한다() {
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(() -> new Menu("짜장", BigDecimal.valueOf(-1)))
        );
    }
}
