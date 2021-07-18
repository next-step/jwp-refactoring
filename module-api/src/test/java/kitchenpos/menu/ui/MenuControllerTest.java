package kitchenpos.menu.ui;

import kitchenpos.menu.MenuTestFixture;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.ui.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MenuRestController.class)
public class MenuControllerTest extends ControllerTest<MenuRequest> {

    private static final String BASE_URI = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @DisplayName("메뉴 생성요청")
    @Test
    void 메뉴_생성요청() throws Exception {
        //Given
        when(menuService.create(any())).thenReturn(MenuTestFixture.맥모닝콤보_응답);

        //When
        ResultActions 결과 = postRequest(BASE_URI, MenuTestFixture.맥모닝콤보_요청);

        //Then
        생성성공(결과);
    }

    @DisplayName("메뉴 목록 조회요청")
    @Test
    void 메뉴_목록_조회요청() throws Exception {
        //Given
        when(menuService.list()).thenReturn(Arrays.asList(MenuTestFixture.맥모닝콤보_응답, MenuTestFixture.메뉴_두번째_응답));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }
}
