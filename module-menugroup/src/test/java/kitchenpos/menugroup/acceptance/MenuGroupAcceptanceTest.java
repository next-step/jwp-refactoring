package kitchenpos.menugroup.acceptance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class MenuGroupAcceptanceTest extends BaseAcceptanceTest {

    MenuGroupRequest 후라이드치킨 = new MenuGroupRequest("후라이드치킨");
    MenuGroupRequest 양념치킨 = new MenuGroupRequest("양념치킨");

    @Test
    void 메뉴그룹을_생성할_수_있다() throws Exception {
        ResultActions resultActions = 메뉴그룹_등록(후라이드치킨);

        메뉴그룹_등록됨(resultActions, 후라이드치킨);
    }

    @Test
    void 메뉴그룹_목록을_조회할_수_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨, 양념치킨);

        ResultActions resultActions = 메뉴그룹_목록_조회();

        메뉴그룹_목록조회_성공(resultActions, 후라이드치킨, 양념치킨);
    }

    private void 메뉴그룹_목록조회_성공(ResultActions resultActions, MenuGroupRequest... menuGroups) throws Exception {
        List<String> names = Arrays.stream(menuGroups)
                .map(MenuGroupRequest::getName)
                .collect(Collectors.toList());

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id", Matchers.hasSize(menuGroups.length)))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.[*].name", Matchers.containsInRelativeOrder(names.toArray())));
    }

    private void 메뉴그룹_등록됨(ResultActions resultActions, MenuGroupRequest menuGroup) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(menuGroup.getName()));
    }

    private void 메뉴그룹_등록(MenuGroupRequest... menuGroups) throws Exception {
        for (MenuGroupRequest menuGroup : menuGroups) {
            메뉴그룹_등록(menuGroup);
        }
    }

    private ResultActions 메뉴그룹_등록(MenuGroupRequest menuGroup) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions 메뉴그룹_목록_조회() throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/api/menu-groups")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }
}
