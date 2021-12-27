package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.fixtures.MenuFixtures.*;
import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : MenuRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@DisplayName("메뉴 컨트롤러 테스트")
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    private final Menu menu = 메뉴("양념치킨두마리메뉴", new BigDecimal(32000), 메뉴그룹("두마리메뉴그룹").getId(), Lists.newArrayList(메뉴상품(1L, 2L)));
    private final MenuRequest request = 양념치킨두마리메뉴요청();
    private final MenuResponse response = MenuResponse.of(menu);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 조회한다.")
    public void getMenus() throws Exception {
        // given
        List<MenuResponse> menus = Arrays.asList(response);
        given(menuService.list()).willReturn(menus);

        // when
        ResultActions actions = mockMvc.perform(
            get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("양념치킨두마리메뉴")))
                .andExpect(jsonPath("$[0].menuProducts", hasSize(1)))
                .andDo(print());
    }


    @Test
    @DisplayName("메뉴를 등록한다.")
    public void postMenu() throws Exception {
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(menuService.create(any(MenuRequest.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(
            post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is(request.getName())))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)))
                .andDo(print());
    }
}
