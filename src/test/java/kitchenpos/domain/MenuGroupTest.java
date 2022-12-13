package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    void 메뉴_그룹_명은_필수로_입력해야_한다() {
        ThrowingCallable 메뉴_그룹_명_입력_안한_경우 = () -> new MenuGroup(1L, null);

        assertThatIllegalArgumentException().isThrownBy(메뉴_그룹_명_입력_안한_경우);
    }

}
