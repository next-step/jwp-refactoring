package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.ui.MenuGroupRestController;
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
class MenuGroupRestControllerTest extends IntegrationTestHelper {

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
        MenuGroup menuGroup = new MenuGroup("메뉴그룹1");
        Mockito.when(menuGroupService.create(any())).thenReturn(menuGroup);

        // when
        ResultActions resultActions = 메뉴_그룹_생성_요청(menuGroup);

        // then
        MvcResult mvcResult = 메뉴_그룹_생성_성공(resultActions);
        MenuGroup responseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuGroup.class);
        assertAll(() -> {
            assertThat(responseBody.getName()).isEqualTo(menuGroup.getName());
        });
        Mockito.verify(menuGroupService).create(any());
    }

    @DisplayName("전체 메뉴 그룹 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        MenuGroup menuGroup = new MenuGroup("메뉴그룹1");
        Mockito.when(menuGroupService.list()).thenReturn(Arrays.asList(menuGroup));

        // when
        ResultActions resultActions = 메뉴_그룹_조회_요청();

        // then
        MvcResult mvcResult = 메뉴_그룹_조회_성공(resultActions);
        List<MenuGroup> menuGroups = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<MenuGroup>>(){});
        assertThat(menuGroups).isNotEmpty().hasSize(1);
    }


    private ResultActions 메뉴_그룹_생성_요청(MenuGroup menuGroup) throws Exception {
        return postRequest("/api/menu-groups", menuGroup);
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
