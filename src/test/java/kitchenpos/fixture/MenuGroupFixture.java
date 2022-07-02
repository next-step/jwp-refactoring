package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_요청데이터_생성(String name) {
        return 메뉴_그룹_생성(name);
    }

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

}
