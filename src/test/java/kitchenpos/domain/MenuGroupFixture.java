package kitchenpos.domain;

public class MenuGroupFixture {
    private MenuGroupFixture() {
    }

    public static MenuGroup menuGroupParam(String name) {
        return new MenuGroup(null, name);
    }

    public static MenuGroup savedMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
