package kitchenpos.menu.dto;

import kitchenpos.menu.dto.MenuGroupRequest;

public class MenuGroupRequestTest {

    public static MenuGroupRequest 메뉴그룹_요청_객체_생성(String name) {
        return new MenuGroupRequest.Builder()
                .name(name)
                .build();
    }
}