package kitchenpos.menu.domain.unit;

import static kitchenpos.fixture.MenuProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import kitchenpos.common.*;
import kitchenpos.menu.domain.*;

@DisplayName("메뉴 프로덕트 관련(단위테스트)")
class MenuProductTest {

    @DisplayName("메뉴 프로덕트 생성하기")
    @Test
    void createTest() {
        assertThat(
            MenuProduct.of(메뉴프로덕트_후라이드치킨.getMenuId(), 메뉴프로덕트_후라이드치킨.getProduct(), 메뉴프로덕트_후라이드치킨.getQuantity())
        ).isInstanceOf(MenuProduct.class);
    }

    @DisplayName("빈 상품가진 메뉴프로덕트 생성하면 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(
            () -> MenuProduct.of(메뉴프로덕트_후라이드치킨.getMenuId(), null, 메뉴프로덕트_후라이드치킨.getQuantity())
        ).isInstanceOf(WrongValueException.class);
    }

    @DisplayName("음수인 개수를 가진 메뉴프로덕트 생성하면 실패함")
    @Test
    void exceptionTest2() {
        assertThatThrownBy(
            () -> MenuProduct.of(메뉴프로덕트_후라이드치킨.getMenuId(), 메뉴프로덕트_후라이드치킨.getProduct(), -1L)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
