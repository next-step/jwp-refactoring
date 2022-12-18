package kitchenpos.domain;

public class MainGroupTestFixture {

    public static MenuGroup createMenuGroup(Long id, String name){
        return MenuGroup.of(id, name);
    }
}
