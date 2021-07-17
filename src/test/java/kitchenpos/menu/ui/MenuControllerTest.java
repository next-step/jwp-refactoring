package kitchenpos.menu.ui;

import kitchenpos.common.ui.ControllerTest;
import kitchenpos.common.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
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
public class MenuControllerTest extends ControllerTest<MenuRequest> {

    private static final String BASE_URI = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    private MenuGroup 인기메뉴 = new MenuGroup("인기메뉴");

    private Long 후라이드_ID = 1L;
    private Long 콜라_ID = 2L;
    private MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(후라이드_ID, 1L);
    private MenuProductRequest 콜라_한개_요청 = new MenuProductRequest(콜라_ID, 1L);
    private List<MenuProductRequest> 메뉴상품목록 = Arrays.asList(후라이드_한마리_요청, 콜라_한개_요청);
    private Menu 후라이드세트 = new Menu(1L, "후라이드세트", Price.valueOf(15000), 인기메뉴.getId());
    private MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice().value(), 후라이드세트.getMenuGroupId(), 메뉴상품목록);

    private MenuResponse 메뉴_첫번째_응답 = new MenuResponse(1L, 후라이드세트.getName(), 후라이드세트.getPrice().value(),
            후라이드세트.getMenuGroupId(), new ArrayList<>());
    private MenuResponse 메뉴_두번째_응답 = new MenuResponse(2L, 후라이드세트.getName(), 후라이드세트.getPrice().value(),
            후라이드세트.getMenuGroupId(), new ArrayList<>());

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @DisplayName("메뉴 생성요청")
    @Test
    void 메뉴_생성요청() throws Exception {
        //Given
        when(menuService.create(any())).thenReturn(메뉴_첫번째_응답);

        //When
        ResultActions 결과 = postRequest(BASE_URI,후라이드세트_요청);

        //Then
        생성성공(결과);
    }

    @DisplayName("메뉴 목록 조회요청")
    @Test
    void 메뉴_목록_조회요청() throws Exception {
        //Given
        when(menuService.list()).thenReturn(Arrays.asList(메뉴_첫번째_응답, 메뉴_두번째_응답));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }
}
