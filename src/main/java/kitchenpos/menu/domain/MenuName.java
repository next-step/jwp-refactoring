package kitchenpos.menu.domain;

import javax.persistence.Embeddable;

@Embeddable
public class MenuName {
    private String name;

    public MenuName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
