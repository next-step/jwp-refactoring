package kitchenpos.domain;

public class MenuGroupTestFixture {
    public static MenuGroup menuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
