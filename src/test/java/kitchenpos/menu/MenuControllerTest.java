package kitchenpos.menu;

import kitchenpos.application.MenuService;
import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Menu;
import kitchenpos.ui.MenuRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MenuRestController.class)
public class MenuControllerTest extends ControllerTest<Menu> {

    private static final String MENU_GROUP_URI = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    private Menu 후라이드치킨;

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @BeforeEach
    void 사전준비() {
        후라이드치킨 = new Menu();
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드치킨");
    }

    @DisplayName("메뉴 생성요청")
    @Test
    void 메뉴_생성요청() throws Exception {
        //Given
        when(menuService.create(any())).thenReturn(후라이드치킨);

        //When
        ResultActions 결과 = postRequest(MENU_GROUP_URI, 후라이드치킨);

        //Then
        생성성공(결과, 후라이드치킨);
    }

    @DisplayName("메뉴 목록 조회요청")
    @Test
    void 메뉴_목록_조회요청() throws Exception {
        //Given
        List<Menu> 메뉴_목록 = new ArrayList<>(Arrays.asList(후라이드치킨));
        when(menuService.list()).thenReturn(메뉴_목록);

        //When
        ResultActions 결과 = getRequest(MENU_GROUP_URI);

        //Then
        목록_조회성공(결과, 메뉴_목록);
    }
}
