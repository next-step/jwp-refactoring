package menu.domain.unit;

import static org.assertj.core.api.Assertions.*;

import java.math.*;

import org.junit.jupiter.api.*;

import common.*;
import fixture.*;
import menu.domain.*;

@DisplayName("메뉴 관련(단위테스트)")
class MenuTest {

    @DisplayName("메뉴 생성하기")
    @Test
    void createTest() {
        assertThat(Menu.of(
            MenuFixture.메뉴_후라이드치킨.getName(), MenuFixture.메뉴_후라이드치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴)).isInstanceOf(menu.domain.Menu.class);
    }

    @DisplayName("잘못된 이름(빈값) 가진 메뉴 생성하면 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(
            () -> Menu.of("", MenuFixture.메뉴_후라이드치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴)
        ).isInstanceOf(WrongValueException.class);
    }

    @DisplayName("잘못된 이름(널) 가진 메뉴 생성하면 실패함")
    @Test
    void exceptionTest2() {
        assertThatThrownBy(
            () -> Menu.of(null, MenuFixture.메뉴_후라이드치킨.getPrice(), MenuGroupFixture.메뉴그룹_한마리메뉴)
        ).isInstanceOf(WrongValueException.class);
    }


    @DisplayName("잘못된 가격을 가진 메뉴 생성하면 실패함")
    @Test
    void exceptionTest3() {
        assertThatThrownBy(
            () -> Menu.of(MenuFixture.메뉴_후라이드치킨.getName(), BigDecimal.valueOf(-16000), MenuGroupFixture.메뉴그룹_한마리메뉴)
        ).isInstanceOf(WrongValueException.class);
    }

    @DisplayName("잘못된 메뉴 그룹 가진 메뉴 생성하면 실패함")
    @Test
    void exceptionTest4() {
        assertThatThrownBy(
            () -> Menu.of(MenuFixture.메뉴_후라이드치킨.getName(), MenuFixture.메뉴_후라이드치킨.getPrice(), null)
        ).isInstanceOf(WrongValueException.class);
    }
}
