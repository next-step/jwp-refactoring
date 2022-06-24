package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class Name {

    @Column(nullable = false, name = "name")
    private String value;

    public Name(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
