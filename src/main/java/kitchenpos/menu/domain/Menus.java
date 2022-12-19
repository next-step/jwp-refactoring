package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Menus {

    List<Menu> menus = new ArrayList<>();

    public Menus() {}

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }


    public Stream<Menu> stream() {
        return menus.stream();
    }
}
