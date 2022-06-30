package kitchenpos.utils.generator;

import static kitchenpos.ui.MenuGroupRestControllerTest.MENU_GROUP_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@TestComponent
public class MenuGroupFixtureGenerator {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupFixtureGenerator(MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    private static String NAME = "오늘의 메뉴";
    private static int COUNTER = 0;

    public static MenuGroup generateMenuGroup() {
        COUNTER++;
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(NAME + COUNTER);
        return menuGroup;
    }

    public static MenuGroup generateMenuGroup(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public static List<MenuGroup> generateMenuGroups(int count) {
        List<MenuGroup> menuGroups = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuGroups.add(generateMenuGroup());
        }
        return menuGroups;
    }

    public MenuGroup savedMenuGroup(){
        return menuGroupDao.save(generateMenuGroup());
    }

    public static MockHttpServletRequestBuilder 메뉴_그룹_생성_요청() throws Exception {
        return postRequestBuilder(MENU_GROUP_API_URL_TEMPLATE, generateMenuGroup());
    }

    public static MockHttpServletRequestBuilder 메뉴_그룹_생성_요청(final String name) throws Exception {
        return postRequestBuilder(MENU_GROUP_API_URL_TEMPLATE, generateMenuGroup(name));
    }
}
