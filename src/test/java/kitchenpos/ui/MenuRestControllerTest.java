package kitchenpos.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.IntegrationTestHelper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
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


    @DisplayName("메뉴 생성 테스트")
    @Test
    void createTest() throws Exception {
        // given
        Menu menu = Menu.Builder.of("메뉴1", new BigDecimal(10000)).build();
        Mockito.when(menuService.create(any())).thenReturn(menu);

        // when
        ResultActions resultActions = 메뉴_생성_요청(menu);

        // then
        메뉴_생성_성공(resultActions);
    }

    @DisplayName("전체 메뉴 조회 테스트")
    @Test
    void listTest() throws Exception {
        // given
        Menu menu1 = Menu.Builder.of("메뉴1", new BigDecimal(10000)).build();
        Menu menu2 = Menu.Builder.of("메뉴2", new BigDecimal(15000)).build();
        Mockito.when(menuService.list()).thenReturn(Arrays.asList(menu1, menu2));

        // when
        ResultActions resultActions = 전체_메뉴_조회();

        // then
        MvcResult mvcResult = 전체_메뉴_조회_성공(resultActions);
        List<Menu> menus = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Menu>>(){});
        assertThat(menus).isNotEmpty().hasSize(2);
    }

    private ResultActions 메뉴_생성_요청(final Menu menu) throws Exception {
        return postRequest("/api/menus", menu);
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
