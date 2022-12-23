package kitchenpos.menu.domain;

public class MenuGroupFactory {

    public static MenuGroup create(long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup create(String name) {
        return MenuGroup.from(name);
    }
}
