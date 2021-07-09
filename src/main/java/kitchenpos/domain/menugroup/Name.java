package kitchenpos.domain.menugroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name")
    private String value;

    public Name() {
    }

    private Name(String value) {
        this.value = value;
    }

    public static Name of(String value) {
        return new Name(value);
    }

    public String getValue() {
        return value;
    }
}
