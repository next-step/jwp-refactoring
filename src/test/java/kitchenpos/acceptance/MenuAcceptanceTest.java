package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class MenuAcceptanceTest extends BaseAcceptanceTest {

    ProductRequest 후라이드치킨_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
    ProductRequest 양념치킨_상품 = new ProductRequest(2L, "양념치킨", new BigDecimal(16000.00));
    MenuProduct 후라이드치킨_메뉴상품 = new MenuProduct(1L, 1L, 1L, 1);
    MenuProduct 양념치킨_메뉴상품 = new MenuProduct(2L, 1L, 2L, 1);
    MenuGroup 후라이드치킨_메뉴그룹 = new MenuGroup(1L, "후라이드치킨");
    MenuGroup 양념치킨_메뉴그룹 = new MenuGroup(2L, "양념치킨");
    MenuGroup 두마리치킨_메뉴그룹 = new MenuGroup(1L, "두마리치킨");
    Menu 후라이드치킨_메뉴 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, Collections.singletonList(후라이드치킨_메뉴상품));
    Menu 두마리치킨_메뉴 = new Menu(1L, "두마리치킨", new BigDecimal(40000.00), 1L, Arrays.asList(후라이드치킨_메뉴상품, 양념치킨_메뉴상품));

    @Test
    void 메뉴등록시_가격이_0원_미만이면_오류발생() throws Exception {
        Menu 가격이_0원_미만인_메뉴 = new Menu(1L, "잘못된_가격이_측정된_메뉴", new BigDecimal(-1), 1L, null);

        ResultActions resultActions = 메뉴_등록(가격이_0원_미만인_메뉴);

        메뉴_등록_실패(resultActions);
    }

    @Test
    void 메뉴등록시_가격이_null_이면_오류발생() throws Exception {
        Menu 잘못된_가격이_측정된_메뉴 = new Menu(1L, "잘못된_가격이_측정된_메뉴", null, 1L, null);

        ResultActions resultActions = 메뉴_등록(잘못된_가격이_측정된_메뉴);

        메뉴_등록_실패(resultActions);
    }

    @Test
    void 메뉴의_가격은_메뉴상품들_가격의_합보다_낮아야_한다() throws Exception {
        메뉴그룹_등록(두마리치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        상품_등록(양념치킨_상품);

        ResultActions resultActions = 메뉴_등록(두마리치킨_메뉴);

        메뉴_등록_실패(resultActions);
    }

    @Test
    void 등록_된_메뉴그룹만_지정할_수_있다() throws Exception {
        Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, null);

        ResultActions resultActions = 메뉴_등록(후라이드치킨);

        메뉴_등록_실패(resultActions);
    }

    @Test
    void 등록_된_상품만_지정할_수_있다() throws Exception {
        MenuProduct menuProduct = new MenuProduct(1L, 2L, 1L, 1l);
        Menu 후라이드치킨 = new Menu(1L, "후라이드치킨", new BigDecimal(16000.00), 1L, Collections.singletonList(menuProduct));

        ResultActions resultActions = 메뉴_등록(후라이드치킨);

        메뉴_등록_실패(resultActions);
    }

    @Test
    void 메뉴를_등록할_수_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹, 양념치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);

        ResultActions resultActions = 메뉴_등록(후라이드치킨_메뉴);

        메뉴_등록_성공(resultActions, 후라이드치킨_메뉴);
    }

    @Test
    void 메뉴_목록을_조회할_수_있다() throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹, 양념치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨_메뉴);

        ResultActions resultActions = 메뉴_목록_조회();

        메뉴_목록조회_성공(resultActions, 후라이드치킨_메뉴);
    }

    private ResultActions 메뉴_목록_조회() throws Exception {
        return mvc.perform(get("/api/menus")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 메뉴_목록조회_성공(ResultActions resultActions, Menu menu) throws Exception {
        MenuProduct menuProducts = menu.getMenuProducts().get(0);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(menu.getId()))
                .andExpect(jsonPath("$.[0].name").value(menu.getName()))
                .andExpect(jsonPath("$.[0].price").value(menu.getPrice().floatValue()))
                .andExpect(jsonPath("$.[0].menuGroupId").value(menu.getMenuGroupId()))
                .andExpect(jsonPath("$.[0].menuProducts[0].seq").value(menuProducts.getSeq()))
                .andExpect(jsonPath("$.[0].menuProducts[0].menuId").value(menuProducts.getMenuId()))
                .andExpect(jsonPath("$.[0].menuProducts[0].productId").value(menuProducts.getProductId()))
                .andExpect(jsonPath("$.[0].menuProducts[0].quantity").value(menuProducts.getQuantity()))
        ;
    }

    private ResultActions 메뉴_등록(Menu menu) throws Exception {
        return mvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 메뉴_등록_성공(ResultActions resultActions, Menu menu) throws Exception {
        MenuProduct menuProduct = menu.getMenuProducts().get(0);
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(menu.getId()))
                .andExpect(jsonPath("name").value(menu.getName()))
                .andExpect(jsonPath("price").value(menu.getPrice().floatValue()))
                .andExpect(jsonPath("$.menuProducts[0].seq").value(menuProduct.getSeq()))
                .andExpect(jsonPath("$.menuProducts[0].menuId").value(menuProduct.getMenuId()))
                .andExpect(jsonPath("$.menuProducts[0].productId").value(menuProduct.getProductId()))
                .andExpect(jsonPath("$.menuProducts[0].quantity").value(menuProduct.getQuantity()))
        ;
    }

    private void 메뉴_등록_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private void 메뉴그룹_등록(MenuGroup... menuGroups) throws Exception {
        for (MenuGroup menuGroup : menuGroups) {
            메뉴그룹_등록(menuGroup);
        }
    }

    private ResultActions 메뉴그룹_등록(MenuGroup menuGroup) throws Exception {
        return mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 상품_등록(ProductRequest product) throws Exception {
        return mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
