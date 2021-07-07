package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuGroupRestControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("메뉴그룹 생성 Api 테스트")
    @Test
    void create() throws Exception {
        MenuGroupRequest menuGroup = new MenuGroupRequest("패스트푸드");

        String requestBody = objectMapper.writeValueAsString(menuGroup);

        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @DisplayName("메뉴그룹 목록 Api 테스트")
    @Test
    void list() throws Exception {
        long countOfMenuGroup = menuGroupRepository.count();

        mockMvc.perform(get("/api/menu-groups")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize((int)countOfMenuGroup)))
        ;
    }

}
