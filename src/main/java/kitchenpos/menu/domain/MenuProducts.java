package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> list;

    public MenuProducts() {
        list = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> list) {
        this.list = list;
    }

    public List<MenuProduct> getMenuProducts() {
        return list;
    }
}
