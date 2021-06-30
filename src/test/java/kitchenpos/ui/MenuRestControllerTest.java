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
    void 메뉴의_가격이_비어_있거나_0원보다_적을경우_IllegalArgumentException이_발생한다() throws Exception {
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
    @DisplayName("create - 정상정인 메뉴 등록")
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
                .andExpect(jsonPath("$.id").value(menu.getId()))
                .andExpect(jsonPath("$.name").value(menu.getName()))
                .andExpect(jsonPath("$.price").value(menu.getPrice().getPrice()))
                .andExpect(jsonPath("$.menuGroupId").value(menu.getMenuGroupId()))
                .andExpect(validateMenuProducts(0, menu))
                .andExpect(validateMenuProducts(1, menu))
                .andReturn();
    }

    private ResultMatcher validateMenuProducts(int index, Menu menu) {
        return result -> {
            String jsonPrefix = format("$.menuProducts[%d]", index);
            MenuProduct menuProduct = menu.getMenuProducts().get(index);
            ResultMatcher.matchAll(
                    jsonPath(jsonPrefix + ".seq").value(menuProduct.getSeq()),
                    jsonPath(jsonPrefix + ".menuId").value(menuProduct.getMenuId()),
                    jsonPath(jsonPrefix + ".productId").value(menuProduct.getProductId()),
                    jsonPath(jsonPrefix + ".quantity").value(menuProduct.getQuantity())
            ).match(result);
        };
    }
}