package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.MustHaveName;

@Entity
public class MenuGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MustHaveName name;

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = MustHaveName.valueOf(name);
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public boolean equalName(String menuGroupName) {
        return this.name.equals(menuGroupName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

}
