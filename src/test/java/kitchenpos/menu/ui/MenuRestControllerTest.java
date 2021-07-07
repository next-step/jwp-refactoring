package kitchenpos.menu.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends IntegrationTestHelper {

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    @Override
    protected Object controller() {
        return menuRestController;
    }


    @DisplayName("메뉴 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        MenuRequest menuRequest = MenuRequest.Builder.of("메뉴1", new BigDecimal(10000))
                                                     .build();
        Mockito.when(menuService.create(any())).thenReturn(MenuResponse.of(menuRequest.toMenu()));

        // when
        ResultActions resultActions = 메뉴_생성_요청(menuRequest);

        // then
        메뉴_생성_성공(resultActions);
    }

    @DisplayName("전체 메뉴 조회 요청")
    @Test
    void listTest() throws Exception {
        // given
        Menu menu1 = Menu.Builder.of("메뉴1", new BigDecimal(10000)).build();
        Menu menu2 = Menu.Builder.of("메뉴2", new BigDecimal(15000)).build();
        Mockito.when(menuService.list()).thenReturn(Arrays.asList(MenuResponse.of(menu1),
                                                                  MenuResponse.of(menu2)));

        // when
        ResultActions resultActions = 전체_메뉴_조회();

        // then
        MvcResult mvcResult = 전체_메뉴_조회_성공(resultActions);
        List<Menu> menus = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Menu>>(){});
        assertThat(menus).isNotEmpty().hasSize(2);
    }

    private ResultActions 메뉴_생성_요청(final MenuRequest request) throws Exception {
        return postRequest("/api/menus", request);
    }

    private MvcResult 메뉴_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 전체_메뉴_조회() throws Exception {
        return getRequest("/api/menus");
    }

    private MvcResult 전체_메뉴_조회_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isOk()).andReturn();
    }
}
