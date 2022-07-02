package kitchenpos.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.api.MenuRestController;
import kitchenpos.service.menu.application.MenuService;
import kitchenpos.service.menu.dto.MenuRequest;
import kitchenpos.service.menu.dto.MenuResponse;
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
class MenuRestControllerTest {
    private MockMvc mockMvc;
    @Mock
    private MenuService menuService;
    @InjectMocks
    private MenuRestController menuRestController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuRestController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void test_get() throws Exception {
        //given
        given(menuService.list()).willReturn(Collections.singletonList(new MenuResponse()));

        //then
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void test_post() throws Exception {
        //given
        given(menuService.create(any())).willReturn(new MenuResponse());

        //then
        mockMvc.perform(post("/api/menus")
                        .content(objectMapper.writeValueAsString(new MenuRequest(
                                "name",
                                10L,
                                1L,
                                Collections.emptyList()
                        )))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
