package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private MenuGroupService menuGroupService;


    @DisplayName("메뉴그룹 등록")
    @Test
    public void create() throws Exception {
        MenuGroup expectedMenuGroup = new MenuGroup(1L, "세일");
        given(menuGroupService.create(any())).willReturn(expectedMenuGroup);
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(expectedMenuGroup)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴그룹 리스트")
    @Test
    public void list() throws Exception {
        MenuGroup menuGroup1 = new MenuGroup("그룹1");
        MenuGroup menuGroup2 = new MenuGroup("그룹2");
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2) ;
        given(menuGroupService.list()).willReturn(menuGroups);
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("그룹1"))
                .andExpect(jsonPath("$.[1].name").value("그룹2"));
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}