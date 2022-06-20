package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuGroupFixture {

    public static MenuGroup 메뉴묶음_요청데이터_생성(String name) {
        return 메뉴묶음_데이터_생성(null, name);
    }

    public static MenuGroup 메뉴묶음_데이터_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static void 메뉴묶음_확인(MenuGroup menuGroup, Long id, String name) {
        assertAll(
                () -> assertEquals(id, menuGroup.getId()),
                () -> assertEquals(name, menuGroup.getName())
        );
    }
}
