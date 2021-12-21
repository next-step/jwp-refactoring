package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import org.springframework.util.Assert;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, Name name) {
        Assert.notNull(name, "메뉴 그룹의 이름은 반드시 존재해야 합니다.");

        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, Name name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(Name name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}
