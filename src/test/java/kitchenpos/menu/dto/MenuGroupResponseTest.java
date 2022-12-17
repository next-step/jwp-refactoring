package kitchenpos.menu.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupResponseTest {

    public static MenuGroupResponse 메뉴그룹_응답_객체_생성(Long id, String name) {
        return new MenuGroupResponse.Builder()
                .id(id)
                .name(name)
                .build();
    }

    public static MenuGroupResponse 메뉴그룹_응답_객체_생성(MenuGroup menuGroup) {
        return new MenuGroupResponse.Builder()
                .id(menuGroup.getId())
                .name(menuGroup.getNameValue())
                .build();
    }

    public static List<MenuGroupResponse> 메뉴그룹_응답_객체들_생성(MenuGroup... menuGroups) {
        return Arrays.stream(menuGroups)
                .map(MenuGroupResponseTest::메뉴그룹_응답_객체_생성)
                .collect(Collectors.toList());
    }
}