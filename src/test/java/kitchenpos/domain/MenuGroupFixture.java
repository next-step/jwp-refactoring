package kitchenpos.domain;

public class MenuGroupFixture {
    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(null, name);
    }
}
