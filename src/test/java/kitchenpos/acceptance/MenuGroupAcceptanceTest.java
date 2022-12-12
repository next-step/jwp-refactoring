package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

public class MenuGroupAcceptanceTest extends MockMvcAcceptanceTest {

    /**
     * given: 메뉴 그룹 정보를 입력하고
     * when: 메뉴 그룹 추가를 요청하면
     * then: 추가된 메뉴 그룹이 조회된다.
     */
    @Test
    @DisplayName("메뉴 그룹 추가")
    void createMenuGroup() throws Exception {
        // given

        // when
        ResultActions 메뉴_그룹_추가_요청_결과 = 메뉴_그룹_추가_요청("메뉴그룹 1");

        // then
        메뉴_그룹_추가_요청_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    /**
     * given:
     * when: 메뉴 그룹 전체 조회를 요청하면
     * then: 메뉴 그룹 목록이 조회된다.
     */
    @Test
    @DisplayName("메뉴 그룹 전체 조회")
    void listMenuGroup() throws Exception {
        // given
        메뉴_그룹_추가_요청("메뉴그룹 1");
        메뉴_그룹_추가_요청("메뉴그룹 2");

        // when
        ResultActions 메뉴_그룹_전체_조회_요청_결과 = 메뉴_그룹_전체_조회_요청();

        // then
        메뉴_그룹_전체_조회_요청_결과
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").isArray())
                .andDo(print());

    }

    private ResultActions 메뉴_그룹_전체_조회_요청() throws Exception {
        return mockGet("/api/menu-groups");
    }

    private ResultActions 메뉴_그룹_추가_요청(String name) throws Exception {
        return mockPost("/api/menu-groups", new MenuGroup(name));
    }

}
