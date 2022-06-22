package kitchenpos.helper;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupFixtures {
    public static MenuGroupRequest 인기메뉴_그룹_요청 = new MenuGroupRequest(null, "인기 메뉴");
    public static MenuGroup 두마리메뉴_그룹 = new MenuGroup(1L, "두마리메뉴");
    public static MenuGroup 한마리메뉴_그룹 = new MenuGroup(2L, "한마리메뉴");
    public static MenuGroup 순살파닭두마리메뉴_그룹 = new MenuGroup(3L, "순살파닭두마리메뉴");
    public static MenuGroup 신메뉴_그룹 = new MenuGroup(4L, "한마리메뉴");
    public static MenuGroup 없는메뉴_그룹 = new MenuGroup(999999999999L, "한마리메뉴");


}
