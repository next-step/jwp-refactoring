package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Menus {
    private List<Menu> value = new ArrayList<>();

    public Menus(Collection<Menu> menus) {
        this.value.addAll(menus);
    }

    public List<MenuResponse> toResponse() {
        return this.value.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public List<Menu> getValue() {
        return value;
    }
}
