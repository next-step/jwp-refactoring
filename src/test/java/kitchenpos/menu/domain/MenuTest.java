package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 Domain 단위 테스트")
class MenuTest {
    
    @DisplayName("메뉴 가격이 null 이거나 0원 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu(null, "빅맥세트", -1));
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu(null, "빅맥세트", null));
    }

}
