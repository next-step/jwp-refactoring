package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.common.exception.Message;
import org.apache.logging.log4j.util.Strings;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup of(String name) {
        return new MenuGroup(null, name);
    }

    private static void validIsNotNull(String name) {
        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException(Message.MENU_GROUP_NAME_IS_NOT_NULL.getMessage());
        }
    }

    private MenuGroup(Long id, String name) {
        validIsNotNull(name);
        this.id = id;
        this.name = name;
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
