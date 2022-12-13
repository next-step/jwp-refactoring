package kitchenpos.domain;

import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class MenuGroupName {

    private String name;

    public MenuGroupName() {
    }

    public MenuGroupName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

