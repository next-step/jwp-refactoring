package kitchenpos.common.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Name {
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
