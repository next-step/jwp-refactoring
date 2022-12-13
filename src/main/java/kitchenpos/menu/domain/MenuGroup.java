package kitchenpos.menu.domain;

import io.micrometer.core.instrument.util.StringUtils;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        validateMenuGroupName(name);
        return new MenuGroup(id,name);
    }

    private static void validateMenuGroupName(String name) {
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
