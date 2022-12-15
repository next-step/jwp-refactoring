package kitchenpos.menugroup.dto;

public class MenuGroupRequestTest {

    public static MenuGroupRequest 메뉴그룹_요청_객체_생성(String name) {
        return new MenuGroupRequest.Builder()
                .name(name)
                .build();
    }
}