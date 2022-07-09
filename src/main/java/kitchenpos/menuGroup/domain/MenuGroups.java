package kitchenpos.menuGroup.domain;

import kitchenpos.menuGroup.dto.MenuGroupResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroups {
    List<MenuGroup> value = new ArrayList<>();

    public MenuGroups(List<MenuGroup> menuGroups) {
        this.value.addAll(menuGroups);
    }

    public List<MenuGroupResponse> toResponse() {
        return this.value.stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }

    public List<MenuGroup> getValue() {
        return value;
    }
}
