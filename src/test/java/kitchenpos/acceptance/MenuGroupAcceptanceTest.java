package kitchenpos.acceptance;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MenuGroupAcceptanceTest extends BaseAcceptanceTest {

    MenuGroup 후라이드치킨 = new MenuGroup(1L, "후라이드치킨");
    MenuGroup 양념치킨 = new MenuGroup(2L, "양념치킨");

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

    private void 메뉴그룹_목록조회_성공(ResultActions resultActions, MenuGroup... menuGroups) throws Exception {
        List<String> names = Arrays.stream(menuGroups)
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id", hasSize(menuGroups.length)))
                .andExpect(jsonPath("$.[*].name", containsInRelativeOrder(names.toArray())));
    }

    private void 메뉴그룹_등록됨(ResultActions resultActions, MenuGroup menuGroup) throws Exception {
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(menuGroup.getId()))
                .andExpect(jsonPath("name").value(menuGroup.getName()));
    }

    private void 메뉴그룹_등록(MenuGroup... menuGroups) throws Exception {
        for (MenuGroup menuGroup : menuGroups) {
            메뉴그룹_등록(menuGroup);
        }
    }

    private ResultActions 메뉴그룹_등록(MenuGroup menuGroup) throws Exception {
        return mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 메뉴그룹_목록_조회() throws Exception {
        return mvc.perform(get("/api/menu-groups")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
