package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.embeddableEntity.Name;
import kitchenpos.embeddableEntity.Price;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.fixture.TestMenuRequestFactory;
import kitchenpos.menu.ui.MenuRestController;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
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
    private MenuRequest 메뉴_요청;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(menuRestController).build();
        MenuProductRequest 메뉴_진매 = new MenuProductRequest(1L, 1);
        메뉴_요청 = TestMenuRequestFactory.toMenuRequest("메뉴", 5000, 1L, Arrays.asList(메뉴_진매));
        메뉴 = Menu.of(메뉴_요청, MenuProducts.of(Collections.singletonList(new MenuProduct(1L, new Product(1L, new Name("진매"), new Price(5000)), 1))));
    }

    @Test
    void post() throws Exception {
        //given
        given(menuService.create(any())).willReturn(MenuResponse.of(메뉴));

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URI)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(메뉴_요청)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("메뉴"))
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        // given
        given(menuService.list()).willReturn(Collections.singletonList(MenuResponse.of(메뉴)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URI)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("메뉴"));
    }
}
