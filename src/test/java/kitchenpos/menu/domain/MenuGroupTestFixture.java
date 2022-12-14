package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupTestFixture {
    public static MenuGroupRequest 면류_요청 = menuGroupRequest("면류");
    public static MenuGroupRequest 요리류_요청 = menuGroupRequest("요리류");
    public static MenuGroupRequest 세트류_요청 = menuGroupRequest("세트류");
    public static MenuGroup 면류 = menuGroup(1L, "면류");
    public static MenuGroup 요리류 = menuGroup(2L, "요리류");
    public static MenuGroup 세트류 = menuGroup(3L, "세트류");

    public static MenuGroupRequest menuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static MenuGroup menuGroup(Long id, String name) {
        return MenuGroup.from(id, name);
    }
}
