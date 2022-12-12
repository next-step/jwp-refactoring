package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

public class MenuAcceptanceTest extends MockMvcAcceptanceTest{

    @Autowired
    MenuService menuService;

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    /**
     * Feature: 메뉴 신규 추가
     *  Background:
     *      given: 상품이 등록되어 있고,
     *      And: 메뉴 그룹이 등록되어 있음
     *
     *  Scenario: 메뉴 추가
     *      given: 메뉴 정보를 입력하고
     *      when: 메뉴 등록을 시도하면
     *      then: 등록된 메뉴가 조회된다.
     */
    @Test
    @DisplayName("메뉴 등록")
    void createTest() throws Exception {
        // given
        MenuGroup 메뉴그룹 = menuGroupService.create(new MenuGroup("메뉴 그룹 1"));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));

        String menuName = "메뉴 1";
        Integer price = 3000;

        // when
        ResultActions 메뉴_등록_요청_결과 = 메뉴_등록_요청(menuName, price, 메뉴그룹, 상품1, 1, 상품2, 2);

        // then
        메뉴_등록_요청_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    private ResultActions 메뉴_등록_요청(String menuName, Integer price, MenuGroup 메뉴그룹, Product 상품1, long count1, Product 상품2,
                                   long count2) throws Exception {
        Menu menu = new Menu(menuName, new BigDecimal(price), 메뉴그룹.getId(), Arrays.asList(
                new MenuProduct(상품1.getId(), count1), new MenuProduct(상품2.getId(), count2)
        ));
        return mockPost("/api/menus", menu);
    }

    /**
     * given:
     * when: 메뉴 전체 조회를 시도하면
     * then: 모든 메뉴가 조회된다.
     */
    @Test
    @DisplayName("메뉴 전체 조회")
    void listTest(){

    }

    private ResultActions 상품_등록_요청(String name, Integer price) throws Exception {
        return mockPost("/api/products", new Product(name, new BigDecimal(price)));
    }
    private ResultActions 메뉴_그룹_추가_요청(String name) throws Exception {
        return mockPost("/api/menu-groups", new MenuGroup(name));
    }

}
