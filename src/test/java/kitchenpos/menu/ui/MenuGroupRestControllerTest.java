package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MenuGroupRestControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private MenuGroupRestController menuGroupRestController;
    @Autowired
    ObjectMapper objectMapper;

    MenuGroupRequest 메뉴그룹_한마리메뉴_리퀘스트;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        메뉴그룹_한마리메뉴_리퀘스트 = new MenuGroupRequest("한마리메뉴");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create() throws Exception {
        //given
        String requestBody = objectMapper.writeValueAsString(메뉴그룹_한마리메뉴_리퀘스트);

        //when && then
        mockMvc
                .perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("전체 메뉴그룹을 조회한다.")
    void list() throws Exception {
        //when && then
        mockMvc
                .perform(get("/api/menu-groups"))
                .andExpect(status().isOk());
    }
}