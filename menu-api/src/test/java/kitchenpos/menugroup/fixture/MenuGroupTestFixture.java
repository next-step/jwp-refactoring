package kitchenpos.menugroup.fixture;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;

public class MenuGroupTestFixture {

    public static MenuGroupRequest 메뉴그룹(Long id, String name) {
        return MenuGroupRequest.of(id, name);
    }

    public static MenuGroupRequest 메뉴그룹(String name) {
        return MenuGroupRequest.of(null, name);
    }

    public static MenuGroupRequest 중국집1인메뉴세트그룹요청() {
        return 메뉴그룹(null, "중국집1인메뉴세트그룹");
    }

    public static MenuGroup 중국집1인메뉴세트그룹(final MenuGroupRequest menuGroupRequest) {
        return MenuGroup.of(menuGroupRequest.getName());
    }
}
