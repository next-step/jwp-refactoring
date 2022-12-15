package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuNameTest {

    @Test
    void 메뉴명은_필수로_입력되어야_한다() {
        ThrowingCallable 메뉴명을_입력하지_않은_경우 = () -> new MenuName(null);

        assertThatIllegalArgumentException().isThrownBy(메뉴명을_입력하지_않은_경우);
    }
}
