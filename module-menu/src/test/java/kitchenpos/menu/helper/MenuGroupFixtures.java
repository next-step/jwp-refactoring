package kitchenpos.menu.helper;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupFixtures {
    public static MenuGroupRequest 인기메뉴_그룹_요청 = 메뉴_그룹_요청_만들기("인기 메뉴");
    public static MenuGroup 두마리메뉴_그룹 = 메뉴_그룹_만들기(1L, "두마리메뉴");
    public static MenuGroup 한마리메뉴_그룹 = 메뉴_그룹_만들기(2L, "한마리메뉴");
    public static MenuGroup 순살파닭두마리메뉴_그룹 = 메뉴_그룹_만들기(3L, "순살파닭두마리메뉴");
    public static MenuGroup 신메뉴_그룹 = 메뉴_그룹_만들기(4L, "한마리메뉴");
    public static MenuGroup 없는메뉴_그룹 = 메뉴_그룹_만들기(999999999999L, "한마리메뉴");


    public static MenuGroupRequest 메뉴_그룹_요청_만들기(String name){
        return new MenuGroupRequest(null, name);
    }

    public static MenuGroup 메뉴_그룹_만들기(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup 메뉴_그룹_만들기(String name) {
        return new MenuGroup(null, name);
    }
}
