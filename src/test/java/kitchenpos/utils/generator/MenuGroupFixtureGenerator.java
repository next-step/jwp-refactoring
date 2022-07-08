package kitchenpos.utils.generator;

import static kitchenpos.ui.MenuGroupRestControllerTest.MENU_GROUP_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@TestComponent
public class MenuGroupFixtureGenerator {

    private static String NAME = "오늘의 메뉴";
    private static int COUNTER = 0;

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup 메뉴_그룹_생성() {
        COUNTER++;
        return 메뉴_그룹_생성(NAME + COUNTER);
    }

    public static MenuGroupRequest 메뉴_그룹_생성_요청_객체(String name) {
        return new MenuGroupRequest(name);
    }

    public static List<MenuGroup> 메뉴_그룹_목록_생성(int count) {
        List<MenuGroup> menuGroups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuGroups.add(메뉴_그룹_생성());
        }
        return menuGroups;
    }

    public static MockHttpServletRequestBuilder 메뉴_그룹_생성_요청(final String name) throws Exception {
        return postRequestBuilder(MENU_GROUP_API_URL_TEMPLATE, 메뉴_그룹_생성_요청_객체(name));
    }
}
