package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MenuRestController menuRestController;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MenuService menuService;

    Menu 메뉴;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        메뉴 = new Menu();
        메뉴.setId(1L);
        메뉴.setName("후라이드+후라이드");
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(메뉴);

        when(menuService.create(any())).thenReturn(메뉴);

        //when
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() throws Exception {
        //given
        when(menuService.list()).thenReturn(Arrays.asList(메뉴));

        //when && then
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("후라이드+후라이드")));
    }
}