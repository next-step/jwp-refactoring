package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {
    private MenuGroup 메뉴그룹 = new MenuGroup("메뉴그룹");

    @DisplayName("메뉴 생성 테스트 - 0원보다 작은 경우")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-5, -100})
    void newMenuTest(long price) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu("허니콤보세트", BigDecimal.valueOf(price), 메뉴그룹));
    }

    @DisplayName("메뉴 생성 테스트 - 메뉴 그룹에 속하지 않음")
    @Test
    void newMenuTest2() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Menu("허니콤보세트", BigDecimal.valueOf(20000), null));
    }

}