package kitchenpos.common.wrap;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

@Embeddable
@AccessType(Type.FIELD)
public class Name {

    @Column(nullable = false)
    private String name;

    protected Name() {

    }

    private Name(String name) {
        this.name = name;
    }

    public static Name from(String name) {
        return new Name(name);
    }

    public String getName() {
        return name;
    }
}
