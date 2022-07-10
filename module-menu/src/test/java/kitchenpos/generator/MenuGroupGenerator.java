package kitchenpos.generator;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupCreateRequest;

public class MenuGroupGenerator {

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(String name) {
        return new MenuGroupCreateRequest(name);
    }
}
