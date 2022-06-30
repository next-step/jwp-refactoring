package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {
    @Column(nullable = false)
    private String name;

    protected MenuGroupName() {

    }

    public MenuGroupName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
