package kitchenpos.menugroup.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.Name;

@Entity
public class MenuGroup {
    public static String ENTITY_NAME = "메뉴그룹";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    protected MenuGroup() {}

    private MenuGroup(String name) {
        this.name = Name.of(name);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
