package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {
    private MenuGroup 메뉴그룹 = new MenuGroup("메뉴그룹");

    @DisplayName("메뉴의 가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-5, -100})
    void 가격이_음수인_메뉴_생성(long price) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu("허니콤보세트", BigDecimal.valueOf(price), 메뉴그룹));
    }

    @DisplayName("메뉴 그룹에 속하지 않은 메뉴는 생성할 수 없다.")
    @Test
    void 메뉴그룹이_없는_메뉴_생성() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu("허니콤보세트", BigDecimal.valueOf(20000), null));
    }
}
