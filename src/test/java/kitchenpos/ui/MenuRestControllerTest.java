package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
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
class MenuRestControllerTest {
    private static final String URI = "/api/menus";

    @Mock
    private MenuService menuService;

    @InjectMocks
    private ObjectMapper objectMapper;
    @InjectMocks
    private MenuRestController menuRestController;

    private MockMvc mockMvc;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuRestController).build();
        메뉴 = new Menu();
        메뉴.setName("메뉴");
    }

    @Test
    void post() throws Exception {
        //given
        given(menuService.create(any())).willReturn(메뉴);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(메뉴)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("메뉴"))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(menuService.list()).willReturn(Collections.singletonList(메뉴));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("메뉴"));
    }
}
