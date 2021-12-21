package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProductGroup {
    @OneToMany
    @JoinColumn(name = "menuId")
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProductGroup() {
    }

    private MenuProductGroup(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public static MenuProductGroup of(List<MenuProduct> menuProducts) {
        return new MenuProductGroup(menuProducts);
    }


    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
