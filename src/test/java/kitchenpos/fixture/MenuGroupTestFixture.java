package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;

public class MenuGroupTestFixture {

    public static MenuGroupRequest createMenuGroup(Long id, String name) {
        return MenuGroupRequest.of(id, name);
    }

    public static MenuGroupRequest createMenuGroup(String name) {
        return MenuGroupRequest.of(null, name);
    }

    public static MenuGroupRequest 중국집_1인_메뉴_세트_요청() {
        return createMenuGroup(null, "중국집_1인_메뉴_세트");
    }

    public static MenuGroup 중국집_1인_메뉴_세트(final MenuGroupRequest menuGroupRequest) {
        return MenuGroup.of(menuGroupRequest.getName());
    }
}
