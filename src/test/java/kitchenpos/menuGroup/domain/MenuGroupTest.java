package kitchenpos.menuGroup.domain;

public class MenuGroupTest {

    public static MenuGroup 메뉴그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
