package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 Domain 단위 테스트")
class MenuTest {

    @DisplayName("메뉴 가격은 총 금액을 넘을 수 없다.")
    @Test
    void validateAmount() {
        //given
        Amounts amounts = new Amounts();
        amounts.addAmount(new Amount(6000, 1));
        amounts.addAmount(new Amount(5000, 1));
        Menu menu = new Menu(1L, "간장치킨 세트", 20000);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menu.validateAmount(amounts));
    }

    @DisplayName("메뉴 가격이 null 이거나 0원 미만일 수 없다.")
    @Test
    void validate() {
        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu(null, "빅맥세트", -1));
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu(null, "빅맥세트", null));
    }

}
