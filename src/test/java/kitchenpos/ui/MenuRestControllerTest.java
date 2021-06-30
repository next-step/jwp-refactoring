package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                "Menu",
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

//    @Test
//    @DisplayName("create - 정상정인 메뉴 등록")
//    void 정상적인_메뉴_등록() {
//        // given
//        Long menuGroupId = 1L;
//
//        Product product = new Product(simpleProductId, "PRODUCT", BigDecimal.valueOf(100));
//
//        Menu menu = new Menu(simpleMenuId,
//                "Menu",
//                BigDecimal.valueOf(10),
//                menuGroupId,
//                Arrays.asList(simpleMenuProduct));
//
//        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
//        given(productDao.findById(simpleProductId)).willReturn(Optional.of(product));
//
//        // when
//        when(menuDao.save(menu)).thenReturn(menu);
//        when(menuProductDao.save(simpleMenuProduct)).thenReturn(simpleMenuProduct);
//
//        Menu savedMenu = menuService.create(menu);
//
//        // then
//        assertThat(savedMenu.getMenuProducts())
//                .map(item -> item.getMenuId())
//                .containsExactly(simpleMenuId);
//
//        verify(menuGroupDao, VerificationModeFactory.times(1)).existsById(menuGroupId);
//        verify(productDao, VerificationModeFactory.times(1))
//                .findById(simpleProductId);
//        verify(menuDao, VerificationModeFactory.times(1)).save(menu);
//        verify(menuProductDao, VerificationModeFactory.times(1)).save(simpleMenuProduct);
//    }




}