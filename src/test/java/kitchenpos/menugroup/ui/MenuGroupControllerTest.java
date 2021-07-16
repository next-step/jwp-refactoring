package kitchenpos.menugroup.ui;

import kitchenpos.common.ui.ControllerTest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupControllerTest extends ControllerTest<MenuGroup> {

    private static final String BASE_URI = "/api/menu-groups";

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    private MenuGroupRequest 첫번째_메뉴그룹_요청 = new MenuGroupRequest("첫번째_메뉴그룹");
    private MenuGroupRequest 두번째_메뉴그룹_요청 = new MenuGroupRequest("두번째_메뉴그룹");
    private MenuGroupResponse 첫번째_메뉴그룹_응답 = new MenuGroupResponse(1L, "첫번째_메뉴그룹");
    private MenuGroupResponse 두번째_메뉴그룹_응답 = new MenuGroupResponse(2L, "두번째_메뉴그룹");

    @Override
    protected Object controller() {
        return menuGroupRestController;
    }


    @DisplayName("메뉴그룹 생성요청")
    @Test
    void 메뉴그룹_생성요청() throws Exception {
        //Given
        when(menuGroupService.create(any())).thenReturn(MenuGroupResponse.of(첫번째_메뉴그룹_요청.toMenuGroup()));

        //When
        ResultActions 결과 = postRequest(BASE_URI, 첫번째_메뉴그룹_요청.toMenuGroup());

        //Then
        생성성공(결과);
        결과.andExpect(jsonPath("$.name").value(첫번째_메뉴그룹_요청.getName()));
    }

    @DisplayName("메뉴그룹 목록 조회요청")
    @Test
    void 메뉴그룹_목록_조회요청() throws Exception {
        //Given
        List<MenuGroupRequest> 메뉴그룹_목록 = new ArrayList<>(Arrays.asList(첫번째_메뉴그룹_요청, 두번째_메뉴그룹_요청));
        when(menuGroupService.list()).thenReturn(Arrays.asList(첫번째_메뉴그룹_응답, 두번째_메뉴그룹_응답));

        //When
        ResultActions 결과 = getRequest(BASE_URI);

        //Then
        조회성공(결과);
    }
}
