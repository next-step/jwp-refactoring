package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuGroupAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;


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
        메뉴_그룹_추가_요청("메뉴그룹 1");

        // then

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
        메뉴_그룹_전체_조회_요청()
        ;

        // then

    }

    private ResultActions 메뉴_그룹_전체_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").isArray())
                .andDo(print());
    }

    private void 메뉴_그룹_추가_요청(String name) throws Exception {
        String content = objectMapper.writeValueAsString(new MenuGroup(name));

        mockMvc.perform(post("/api/menu-groups")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

}
