package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴묶음_요청데이터_생성(String name) {
        return 메뉴묶음_데이터_생성(null, name);
    }

    public static MenuGroup 메뉴묶음_데이터_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

}
