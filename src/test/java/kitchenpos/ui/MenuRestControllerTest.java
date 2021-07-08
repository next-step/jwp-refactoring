package kitchenpos.ui;

import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 통합테스트")
class MenuRestControllerTest extends IntegrationSupportTest {
    private static final String URI = "/api/menus";

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(postAsJson(URI, MenuRequest.of(
                "1인세트",
                BigDecimal.valueOf(11000),
                1L,
                Lists.list(MenuProductRequest.of(1L, 1))
        )));

        //then
        actions.andExpect(status().isCreated());
        //and then
        MenuResponse response = toObject(actions.andReturn(), MenuResponse.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("1인세트");
        assertThat(response.getPrice().longValue()).isEqualTo(11000L);
        assertThat(response.getMenuGroupId()).isEqualTo(1L);
        assertThat(response.getMenuProducts()).isNotEmpty();
    }

    @DisplayName("메뉴를 모두 조회한다.")
    @Test
    void list() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk());
        //and then
        List<MenuResponse> response = toList(actions.andReturn(), MenuResponse.class);
        assertThat(response).isNotEmpty();
    }
}
