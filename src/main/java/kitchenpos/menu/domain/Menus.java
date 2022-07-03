package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuResponse;

import java.util.*;
import java.util.stream.Collectors;

public class Menus {
    private List<Menu> value = new ArrayList<>();

    public Menus(Collection<Menu> menus) {
        this.value.addAll(menus);
    }

    public List<MenuResponse> toResponse() {
        return this.value.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public Menu findMenuById(Long id) {
        return this.value.stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean isNotAllContainIds(Collection<Long> ids) {
        Set<Long> removeDuplicatedIds = new HashSet<>(ids);

        return this.value.size() != removeDuplicatedIds.size() ||
                this.value.stream().map(Menu::getId).anyMatch(id -> !removeDuplicatedIds.contains(id));
    }

    public List<Menu> getValue() {
        return value;
    }
}
