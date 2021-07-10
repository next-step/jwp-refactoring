package kitchenpos.menugroup.ui;

import kitchenpos.common.ControllerTest;
import kitchenpos.menugroup.application.MenuGroupRestController;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupControllerTest extends ControllerTest<MenuGroup> {

    private static final String BASE_URI = "/api/menu-groups";

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    private MenuGroupRequest 첫번째_메뉴그룹;
    private MenuGroupRequest 두번째_메뉴그룹;

    @Override
    protected Object controller() {
        return menuGroupRestController;
    }

    @BeforeEach
    void 사전준비() {

        첫번째_메뉴그룹 = new MenuGroupRequest("첫번째_메뉴그룹");
        두번째_메뉴그룹 = new MenuGroupRequest("두번째_메뉴그룹");
    }

    @DisplayName("메뉴그룹 생성요청")
    @Test
    void 메뉴그룹_생성요청() throws Exception {
        //Given
        when(menuGroupService.create(any())).thenReturn(MenuGroupResponse.of(첫번째_메뉴그룹.toMenuGroup()));

        //When
        ResultActions 결과 = postRequest(BASE_URI, 첫번째_메뉴그룹.toMenuGroup());

        //Then
        생성성공(결과, 첫번째_메뉴그룹.toMenuGroup());
    }

    @DisplayName("메뉴그룹 목록 조회요청")
    @Test
    void 메뉴그룹_목록_조회요청() throws Exception {
        //Given
        List<MenuGroupRequest> 메뉴그룹_목록 = new ArrayList<>(Arrays.asList(첫번째_메뉴그룹, 두번째_메뉴그룹));
        when(menuGroupService.newList()).thenReturn(MenuGroupResponse.ofList(MenuGroupRequest.toMenuGroupList(메뉴그룹_목록)));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        목록_조회성공(결과, MenuGroupRequest.toMenuGroupList(메뉴그룹_목록));
    }
}
