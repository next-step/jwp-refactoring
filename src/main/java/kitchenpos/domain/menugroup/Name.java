package kitchenpos.domain.menugroup;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name")
    private String value;

    public Name() { }

    public static Name of(String value){
        return new Name(value);
    }

    private Name(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
