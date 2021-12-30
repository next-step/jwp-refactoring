package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.dto.menu.MenuProductResponse;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.menugroup.MenuGroupResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private MenuService menuService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        final MenuRequest 메뉴_요청 = TestMenuFactory.메뉴_요청("메뉴", 40000, 1L);

        final ProductResponse 상품_응답 = TestProductFactory.상품_응답(1L, "상품", 1000);
        final List<MenuProductResponse> 메뉴상품목록_응답 = TestMenuFactory.메뉴상품목록_응답(상품_응답, 2);
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");
        final MenuResponse 메뉴_응답 = TestMenuFactory.메뉴_응답(1L, "메뉴", 50000, 메뉴그룹_응답, 메뉴상품목록_응답);

        given(menuService.create(any())).willReturn(메뉴_응답);

        final ResultActions actions = mvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(메뉴_요청)))
                .andDo(print());
        
        actions.andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/api/menus/1"))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("메뉴"))
                .andExpect(jsonPath("price").value(50000))
                .andExpect(jsonPath("menuGroupResponse").hasJsonPath())
                .andExpect(jsonPath("menuProductResponses").isArray());
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() throws Exception {
        final ProductResponse 상품_응답 = TestProductFactory.상품_응답(1L, "상품", 1000);
        final List<MenuProductResponse> 메뉴상품목록_응답 = TestMenuFactory.메뉴상품목록_응답(상품_응답, 2);
        final MenuGroupResponse 메뉴그룹_응답 = TestMenuGroupFactory.메뉴그룹_응답(1L, "메뉴그룹");
        final MenuResponse 메뉴_응답 = TestMenuFactory.메뉴_응답(1L, "메뉴", 50000, 메뉴그룹_응답, 메뉴상품목록_응답);

        given(menuService.list()).willReturn(Collections.singletonList(메뉴_응답));

        final ResultActions actions = mvc.perform(get("/api/menus")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("메뉴")));
    }
}
