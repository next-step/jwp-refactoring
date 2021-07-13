package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Menus {

    @OneToMany(mappedBy = "menuGroup")
    private List<Menu> menus = new ArrayList<>();
}
