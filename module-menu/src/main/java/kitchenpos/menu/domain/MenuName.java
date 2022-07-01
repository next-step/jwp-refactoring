package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {
    @Column(nullable = false)
    private String name;

    protected MenuName() {

    }

    public MenuName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
