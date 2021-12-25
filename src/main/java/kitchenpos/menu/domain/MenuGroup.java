package kitchenpos.menu.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = Objects.requireNonNull(name, "메뉴그룹명은 필수입니다.");
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
