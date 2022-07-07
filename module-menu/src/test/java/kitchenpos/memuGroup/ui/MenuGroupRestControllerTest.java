package kitchenpos.memuGroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menuGroup.application.MenuGroupService;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.menuGroup.ui.MenuGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MenuGroupRestControllerTest {
    @Mock
    private MenuGroupService menuGroupService;

    @InjectMocks
    private ObjectMapper objectMapper;
    @InjectMocks
    private MenuGroupRestController menuGroupRestController;

    private MockMvc mockMvc;
    private MenuGroup 메뉴그룹;
    private MenuGroupRequest 메뉴그룹_요청;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController).build();
        메뉴그룹 = new MenuGroup("메뉴그룹");
        메뉴그룹_요청 = new MenuGroupRequest("메뉴그룹");
    }

    @Test
    @DisplayName("메뉴그룹을 등록한다")
    void post() throws Exception {
        // given
        given(menuGroupService.create(any())).willReturn(MenuGroupResponse.of(메뉴그룹));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(메뉴그룹_요청)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴그룹을 조회한다")
    void get() throws Exception {
        // given
        given(menuGroupService.list()).willReturn(Collections.singletonList(MenuGroupResponse.of(메뉴그룹)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }
}
