package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.MenuGroupService;
import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
@AutoConfigureMockMvc
class MenuGroupRestControllerTest extends BaseControllerTest {

    @MockBean
    MenuGroupService mockMenuGroupService;

    MenuGroup 베스트;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() throws Exception {
        베스트 = new MenuGroup();
        베스트.setId(1L);
        베스트.setName("베스트");

        String jsonString = objectMapper.writeValueAsString(베스트);
        when(mockMenuGroupService.create(any())).thenReturn(베스트);

        mockMvc.perform(post("/api/menu-groups")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(jsonString)
                ).andDo(print()) // 요청과 응답을 출력이 가능
                 .andExpect(status().isCreated());
    }


    @DisplayName("메뉴 그룹 조회")
    @Test
    void list() throws Exception {
        when(mockMenuGroupService.list()).thenReturn(Arrays.asList(new MenuGroup(), new MenuGroup()));

        mockMvc.perform(get("/api/menu-groups"))
            .andDo(print()) // 요청과 응답을 출력이 가능
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isArray());
    }
}