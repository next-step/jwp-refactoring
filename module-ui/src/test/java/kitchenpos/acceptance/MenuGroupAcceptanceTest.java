package kitchenpos.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc
public class MenuGroupAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 등록")
    @Test
    public void createMenuGroup() throws Exception {
        MenuGroupResponse menuGroup = new MenuGroupResponse(1L, "그룹");
        given(menuGroupService.create(any())).willReturn(menuGroup);
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴그룹 리스트")
    @Test
    public void selectMenuGroup() throws Exception {
        MenuGroupResponse menuGroup1 = new MenuGroupResponse(1L, "그룹1");
        MenuGroupResponse menuGroup2 = new MenuGroupResponse(2L, "그룹2");
        List<MenuGroupResponse> menuGroups = Arrays.asList(menuGroup1, menuGroup2) ;
        given(menuGroupService.list()).willReturn(menuGroups);
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("그룹1"))
                .andExpect(jsonPath("$.[1].name").value("그룹2"));
    }

    private String serialize(Object request) throws JsonProcessingException {
        mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }
}
