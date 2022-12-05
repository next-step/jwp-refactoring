package kitchenpos.domain;

public class MenuGroupTestFixture {

    public static MenuGroup generateMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroup generateMenuGroup(String name) {
        return MenuGroup.of(null, name);
    }
}
