package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Menus {

    @OneToMany(mappedBy = "menuGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Menu> menus = new ArrayList<>();

    public Menus() {
    }

    public Menus(final List<Menu> menus) {
        this.menus.addAll(menus);
    }

    public void add(final Menu menu) {
        menus.add(menu);
    }

    public int size() {
        return menus.size();
    }

    public Menu get(final Long menuId) {
        return menus.stream()
            .filter(menu -> Objects.equals(menu.getId(), menuId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Menus menus1 = (Menus)o;
        return Objects.equals(menus, menus1.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menus);
    }
}
