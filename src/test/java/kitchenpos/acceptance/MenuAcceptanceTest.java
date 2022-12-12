package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

public class MenuAcceptanceTest extends MockMvcAcceptanceTest{

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

        List<MenuProduct> 구성상품들 = Arrays.asList(
                new MenuProduct(상품1.getId(), 1), new MenuProduct(상품2.getId(), 2)
        );

        // when
        ResultActions 메뉴_등록_요청_결과 = 메뉴_등록_요청("메뉴 1", 3000, 메뉴그룹, 구성상품들);

        // then
        메뉴_등록_요청_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    /**
     * Feature: 메뉴 신규 추가 실패
     *  Background:
     *      given: 메뉴 그룹이 등록되어 있음
     *
     *  Scenario: 상품 미등록으로 인한 메뉴 등록 실패
     *      when: 상품정보 없이 메뉴 등록을 시도하면
     *      then: 메뉴가 등록되지 않는다.
     */
    @Test
    @DisplayName("상품이 없으면 메뉴를 등록할 수 없습니다.")
    void createFailTest1() {
        // given
        MenuGroup 메뉴그룹 = menuGroupService.create(new MenuGroup("메뉴 그룹 1"));

        // when & then
        assertThatThrownBy(
                () -> 메뉴_등록_요청("메뉴 1", 3000, 메뉴그룹, Collections.emptyList())
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Feature: 메뉴 신규 추가 실패
     *  Background:
     *      given: 상품이 미리 등록되어 있음
     *
     *  Scenario: 메뉴그룹 미지정으로 인한 메뉴 등록 실패
     *      when: 메뉴그룹을 지정하지 않고 메뉴 등록을 시도하면
     *      then: 메뉴가 등록되지 않는다.
     */
    @Test
    @DisplayName("메뉴그룹이 지정 되어 있지 않으면 메뉴를 등록할 수 없습니다.")
    void createFailTest2() {
        // given
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));

        List<MenuProduct> 구성상품들 = Arrays.asList(
                new MenuProduct(상품1.getId(), 1), new MenuProduct(상품2.getId(), 2)
        );

        // when & then
        assertThatThrownBy(
                () -> 메뉴_등록_요청("메뉴 1", 3000, new MenuGroup(), 구성상품들)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Feature: 메뉴 신규 추가 실패
     *  Background:
     *      given: 상품이 미리 등록되어 있음
     *      And: 메뉴그룹이 미리 등록되어 있음
     *
     *  Scenario: 가격 음수 지정으로 인한 메뉴 등록 실패
     *      when: 가격을 음수로 입력하고 메뉴를 등록하면
     *      then: 메뉴가 등록되지 않는다.
     */
    @Test
    @DisplayName("가격이 음수로 되어있으면 메뉴를 등록할 수 없습니다.")
    void createFailTest3() {
        // given
        MenuGroup 메뉴그룹 = menuGroupService.create(new MenuGroup("메뉴 그룹 1"));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));

        List<MenuProduct> 구성상품들 = Arrays.asList(
                new MenuProduct(상품1.getId(), 1), new MenuProduct(상품2.getId(), 2)
        );

        // when & then
        assertThatThrownBy(
                () -> 메뉴_등록_요청("메뉴 1", -100, 메뉴그룹, 구성상품들)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Feature: 메뉴 신규 추가 실패
     *  Background:
     *      given: 상품이 미리 등록되어 있음
     *      And: 메뉴그룹이 미리 등록되어 있음
     *
     *  Scenario: 잘못된 가격 산정으로 인한 메뉴 등록 실패
     *      when: 메뉴 가격을 각 상품들의 수량*가격 의 합보다 크게 설정하고 메뉴를 등록하면
     *      then: 메뉴가 등록되지 않는다.
     */
    @Test
    @DisplayName("메뉴 가격이 각 상품들의 수량*가격의 합보다 크면 메뉴를 등록할 수 없습니다.")
    void createFailTest4() {
        // given
        MenuGroup 메뉴그룹 = menuGroupService.create(new MenuGroup("메뉴 그룹 1"));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));

        List<MenuProduct> 구성상품들 = Arrays.asList(
                new MenuProduct(상품1.getId(), 1), new MenuProduct(상품2.getId(), 2)
        );

        // when & then
        assertThatThrownBy(
                () -> 메뉴_등록_요청("메뉴 1", 5001, 메뉴그룹, 구성상품들)
        ).hasCause(new IllegalArgumentException());
    }

    private ResultActions 메뉴_등록_요청(String menuName, Integer price, MenuGroup 메뉴그룹, List<MenuProduct> products) throws Exception {
        Menu menu = new Menu(menuName, new BigDecimal(price), 메뉴그룹.getId(), products);
        return mockPost("/api/menus", menu);
    }

    /**
     * Feature: 메뉴 목록 전체 조회
     *  Background:
     *      given: 메뉴가 여러 개 등록 되어 있음
     *  Scenario: 메뉴 전체 조회
     *      when: 메뉴 전체 조회를 시도하면
     *      then: 모든 메뉴가 조회된다.
     */
    @Test
    @DisplayName("메뉴 전체 조회")
    void listTest() throws Exception {
        // given
        MenuGroup 메뉴그룹 = menuGroupService.create(new MenuGroup("메뉴 그룹 1"));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        메뉴_등록_요청("메뉴 1", 10000, 메뉴그룹, Arrays.asList(
                new MenuProduct(상품1.getId(), 5), new MenuProduct(상품2.getId(), 5)
        ));
        메뉴_등록_요청("메뉴 2", 15000, 메뉴그룹, Arrays.asList(
                new MenuProduct(상품1.getId(), 10), new MenuProduct(상품2.getId(), 7)
        ));
        메뉴_등록_요청("메뉴 3", 7000, 메뉴그룹, Arrays.asList(
                new MenuProduct(상품1.getId(), 5), new MenuProduct(상품2.getId(), 3)
        ));

        // when
        ResultActions 메뉴_전체_조회_결과 = 메뉴_전체_조회();

        // then
        메뉴_전체_조회_결과
                .andExpect(status().isOk())
                .andExpect(jsonPath(".name", Matchers.hasItems("메뉴 1", "메뉴 2", "메뉴 3")))
                .andDo(print());
    }

    private ResultActions 메뉴_전체_조회() throws Exception {
        return mockGet("/api/menus");
    }

}
