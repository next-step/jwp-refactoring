package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {
    @Column
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
