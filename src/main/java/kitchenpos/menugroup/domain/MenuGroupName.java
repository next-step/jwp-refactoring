package kitchenpos.menugroup.domain;

import kitchenpos.menugroup.exception.IllegalMenuGroupNameException;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {
    @Column(nullable = false)
    private String name;

    protected MenuGroupName() {
    }

    private MenuGroupName(String name) {
        validate(name);
        this.name = name;
    }

    public static MenuGroupName of(String name) {
        return new MenuGroupName(name);
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalMenuGroupNameException("이름은 빈값이 될 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }

    public boolean matchName(String targetName) {
        return this.name.equals(targetName);
    }
}
