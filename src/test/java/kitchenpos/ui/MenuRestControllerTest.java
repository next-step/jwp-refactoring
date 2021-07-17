package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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

import kitchenpos.application.MenuService;
import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.Menu;

@SpringBootTest
@AutoConfigureMockMvc
class MenuRestControllerTest extends BaseControllerTest {
    @MockBean
    MenuService mockMenuService;

    @DisplayName("메뉴 생성")
    @Test
    void create() throws Exception {
        Menu 면요리 = new Menu();

        String jsonString = objectMapper.writeValueAsString(면요리);
        when(mockMenuService.create(any())).thenReturn(면요리);

        mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonString)
        ).andDo(print())
        .andExpect(status().isCreated());
    }
    
    @DisplayName("메뉴 조회")
    @Test
    void list() throws Exception {
        when(mockMenuService.list()).thenReturn(Arrays.asList(new Menu(), new Menu()));

        mockMvc.perform(get("/api/menus"))
            .andDo(print()) // 요청과 응답을 출력이 가능
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]").isArray());
    }
}