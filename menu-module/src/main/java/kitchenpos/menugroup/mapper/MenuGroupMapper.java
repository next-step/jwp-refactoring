package kitchenpos.menugroup.mapper;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public static MenuGroupResponse toMenuGroupResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponse> toMenuGroupResponses(final List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                .map(MenuGroupMapper::toMenuGroupResponse)
                .collect(Collectors.toList());
    }
}
