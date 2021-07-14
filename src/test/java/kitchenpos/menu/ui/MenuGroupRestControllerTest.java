package kitchenpos.menu.ui;

import static kitchenpos.menu.domain.MenuGroupTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.utils.IntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("메뉴 그룹 통합 테스트")
class MenuGroupRestControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() throws Exception {
        MenuGroupRequest request = new MenuGroupRequest("추천메뉴");
        mockMvc.perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 가져온다")
    void list() throws Exception {
        mockMvc.perform(get("/api/menu-groups"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(jsonPath("$[1].id").value(한마리메뉴.getId()))
            .andExpect(jsonPath("$[1].name").value(한마리메뉴.getName()));
    }
}
