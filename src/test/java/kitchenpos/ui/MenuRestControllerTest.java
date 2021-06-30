package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static kitchenpos.ui.JsonUtil.toJson;
import static kitchenpos.ui.JsonUtil.toObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuRestController.class)
@ExtendWith(MockitoExtension.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;



    @Test
    @DisplayName("[post]/api/menus - 메뉴의 가격이 비어 있거나, 0원보다 적을경우 BadRequest이다.")
    void 메뉴의_가격이_비어_있거나_0원보다_적을경우_BadRequest이다() throws Exception {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("Menu",
                BigDecimal.valueOf(-1),
                1L,
                Arrays.asList());

        given(menuService.create(any())).willAnswer(i -> i.getArgument(0));

        // when
        MvcResult mvcResult = mockMvc.perform(
                post("/api/menus")
                        .content(toJson(menuCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        assertThat(mvcResult.getResolvedException()).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("[post]/api/menus - 정상정인 메뉴 등록")
    void 정상적인_메뉴_등록() throws Exception {
        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("Menu",
                BigDecimal.valueOf(10),
                1L,
                Arrays.asList(
                        new MenuProduct(1L, 1L, 1L, 1L),
                        new MenuProduct(2L, 2L, 2L, 2L)
                )
        );
        Menu menu = new Menu(
                1L,
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(),
                menuCreateRequest.getMenuProducts()
        );
        given(menuService.create(any())).willReturn(menu);

        // when & then
        mockMvc.perform(
                post("/api/menus")
                        .content(toJson(menuCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(validateMenu("$", menu))
                .andExpect(validateMenuProducts("$.menuProducts[0]", menu.getMenuProducts().get(0)))
                .andExpect(validateMenuProducts("$.menuProducts[1]", menu.getMenuProducts().get(1)))
                .andReturn();
    }

    @Test
    @DisplayName("[get]/api/menus - 정상적인 리스트 조회")
    void 정상적인_리스트_조회() throws Exception {
        List<Menu> menus = Arrays.asList(
                new Menu(1L, "Menu", BigDecimal.valueOf(10), 1L,
                        Arrays.asList(
                                new MenuProduct(1L, 1L, 1L, 1L),
                                new MenuProduct(2L, 2L, 2L, 2L)
                        )
                ),
                new Menu(2L, "Menu2", BigDecimal.valueOf(20), 2L,
                        Arrays.asList(
                                new MenuProduct(3L, 3L, 3L, 3L),
                                new MenuProduct(4L, 4L, 4L, 4L)
                        )
                )
        );

        given(menuService.list()).willReturn(menus);

        // when & then
        mockMvc.perform(
                get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(validateMenu("$[0]", menus.get(0)))
                .andExpect(validateMenuProducts("$[0].menuProducts[0]", menus.get(0).getMenuProducts().get(0)))
                .andExpect(validateMenuProducts("$[0].menuProducts[1]", menus.get(0).getMenuProducts().get(1)))
                .andExpect(validateMenuProducts("$[1].menuProducts[0]", menus.get(1).getMenuProducts().get(0)))
                .andExpect(validateMenuProducts("$[1].menuProducts[1]", menus.get(1).getMenuProducts().get(1)))
                .andReturn();

    }

    private ResultMatcher validateMenu(String expressionPrefix, Menu menu) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(expressionPrefix + ".id").value(menu.getId()),
                    jsonPath(expressionPrefix + ".name").value(menu.getName()),
                    jsonPath(expressionPrefix + ".price").value(menu.getPrice().getPrice()),
                    jsonPath(expressionPrefix + ".menuGroupId").value(menu.getMenuGroupId())
            ).match(result);
        };
    }

    private ResultMatcher validateMenuProducts(String expressionPrefix, MenuProduct menuProduct) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(expressionPrefix + ".seq").value(menuProduct.getSeq()),
                    jsonPath(expressionPrefix + ".menuId").value(menuProduct.getMenuId()),
                    jsonPath(expressionPrefix + ".productId").value(menuProduct.getProductId()),
                    jsonPath(expressionPrefix + ".quantity").value(menuProduct.getQuantity())
            ).match(result);
        };
    }
}