package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Menus {
    @OneToMany
    @JoinColumn(name = "menu_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private List<Menu> menus = new ArrayList<>();

    public void add(Menu menu) {
        menus.add(menu);
    }

    public <T> List<T> mapList(Function<Menu, T> function) {
        return menus.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
