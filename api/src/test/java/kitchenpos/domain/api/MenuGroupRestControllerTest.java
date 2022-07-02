package kitchenpos.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.api.MenuGroupRestController;
import kitchenpos.service.menugroup.application.MenuGroupService;
import kitchenpos.service.menugroup.dto.MenuGroupRequest;
import kitchenpos.service.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MenuGroupRestControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MenuGroupService menuGroupService;
    @InjectMocks
    private MenuGroupRestController menuGroupRestController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController).build();
    }

    @Test
    void test_get() throws Exception {
        //given
        given(menuGroupService.list()).willReturn(Collections.singletonList(new MenuGroupResponse()));

        //then
        mockMvc.perform(get("/api/menu-groups"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_post() throws Exception {
        //given
        MenuGroupRequest request = new MenuGroupRequest("menu");
        given(menuGroupService.create(any())).willReturn(new MenuGroupResponse());

        //then
        mockMvc.perform(post("/api/menu-groups")
                        .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
