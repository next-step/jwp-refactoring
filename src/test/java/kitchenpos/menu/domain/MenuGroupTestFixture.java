package kitchenpos.menu.domain;

public class MenuGroupTestFixture {
    public static MenuGroup menuGroup(Long id, String name) {
        return MenuGroup.from(id, name);
    }
}
