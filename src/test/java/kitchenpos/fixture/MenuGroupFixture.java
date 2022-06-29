package kitchenpos.fixture;

import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.dto.MenuGroupRequestDto;
import kitchenpos.menu_group.dto.MenuGroupResponseDto;

public class MenuGroupFixture {

    public static MenuGroupRequestDto 메뉴묶음_요청데이터_생성(String name) {
        return new MenuGroupRequestDto(name);
    }

    public static MenuGroup 메뉴묶음_데이터_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroupResponseDto 메뉴묶음_응답_데이터_생성(Long id, String name) {
        return new MenuGroupResponseDto(id, name);
    }

}
