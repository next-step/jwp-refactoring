package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected MenuGroup() {
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

    public String getName() {
        return name;
    }
}
