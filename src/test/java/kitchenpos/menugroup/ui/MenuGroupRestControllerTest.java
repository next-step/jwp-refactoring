package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.MockMvcTestHelper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends MockMvcTestHelper {

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @Override
    protected Object controller() {
        return menuGroupRestController;
    }

    @DisplayName("메뉴 그룹 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        MenuGroupRequest expected = new MenuGroupRequest("메뉴그룹1");
        Mockito.when(menuGroupService.create(any())).thenReturn(new MenuGroupResponse());

        // when
        ResultActions resultActions = 메뉴_그룹_생성_요청(expected);

        // then
        메뉴_그룹_생성_성공(resultActions);
        Mockito.verify(menuGroupService).create(any());
    }

    @DisplayName("전체 메뉴 그룹 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        Mockito.when(menuGroupService.list()).thenReturn(Arrays.asList(new MenuGroupResponse()));

        // when
        ResultActions resultActions = 메뉴_그룹_조회_요청();

        // then
        MvcResult mvcResult = 메뉴_그룹_조회_성공(resultActions);
        List<MenuGroupResponse> menuGroups = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                    new TypeReference<List<MenuGroupResponse>>() {});
        assertThat(menuGroups).isNotEmpty().hasSize(1);
    }


    private ResultActions 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) throws Exception {
        return postRequest("/api/menu-groups", menuGroupRequest);
    }

    private MvcResult 메뉴_그룹_생성_성공(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 메뉴_그룹_조회_요청() throws Exception {
        return getRequest("/api/menu-groups");
    }

    private MvcResult 메뉴_그룹_조회_성공(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
