package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Menus {

    @OneToMany(mappedBy = "menuGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    public Menus() {
    }

    public Menus(List<Menu> menus) {
        this.menus.addAll(menus);
    }

    public void add(Menu menu) {
        menus.add(menu);
    }

    public int size() {
        return menus.size();
    }

    public Menu get(Long menuId) {
        return menus.stream()
            .filter(menu -> Objects.equals(menu.getId(), menuId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
