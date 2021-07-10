package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Menus {

    @OneToMany(mappedBy = "menuGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    public void add(Menu menu) {
        menus.add(menu);
    }
}
