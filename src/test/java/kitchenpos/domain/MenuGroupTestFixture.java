package kitchenpos.domain;

public class MenuGroupTestFixture {

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup createMenuGroup(String name) {
        return MenuGroup.of(null, name);
    }
}
