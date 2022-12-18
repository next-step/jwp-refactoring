package kitchenpos.menugroup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.ObjectUtils;

@Entity
public class MenuGroup {
    private static final String NAME_NOT_ALLOW_NULL_OR_EMPTY = "메뉴 그룹명은 비어있거나 공백일 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected MenuGroup() {}

    public MenuGroup(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_NOT_ALLOW_NULL_OR_EMPTY);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
