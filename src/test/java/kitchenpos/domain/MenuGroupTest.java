package kitchenpos.domain;

public class MenuGroupTest {

    public static MenuGroup 메뉴_그룹_생성(String name) {
        MenuGroup result = new MenuGroup();

        result.setName(name);

        return result;
    }
}
