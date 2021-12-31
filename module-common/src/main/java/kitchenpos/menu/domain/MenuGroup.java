package kitchenpos.menu.domain;

import static org.springframework.util.ObjectUtils.*;

import javax.persistence.*;

import kitchenpos.common.*;

@Entity
public class MenuGroup {
    private static final String MENU_GROUP = "메뉴 그룹";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    protected MenuGroup() {

    }

    public MenuGroup(String name) {
        validate(name);
        this.name = name;
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    private void validate(String name) {
        if (isEmpty(name)) {
            throw new NotFoundException(MENU_GROUP);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
