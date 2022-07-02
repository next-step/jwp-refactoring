package kitchenpos.ui;

import static kitchenpos.utils.MockMvcUtil.as;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성_요청;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.utils.BaseTest;
import kitchenpos.utils.generator.MenuGroupFixtureGenerator;
import kitchenpos.utils.generator.ProductFixtureGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Menu")
@Import({MenuGroupFixtureGenerator.class, ProductFixtureGenerator.class})
public class MenuRestControllerTest extends BaseTest {

    private final MenuGroupFixtureGenerator menuGroupFixtureGenerator;
    private final ProductFixtureGenerator productFixtureGenerator;

    public MenuRestControllerTest(
        MenuGroupFixtureGenerator menuGroupFixtureGenerator,
        ProductFixtureGenerator productFixtureGenerator
    ) {
        this.menuGroupFixtureGenerator = menuGroupFixtureGenerator;
        this.productFixtureGenerator = productFixtureGenerator;
    }

    public static final String MENU_API_URL_TEMPLATE = "/api/menus";

    @Test
    @DisplayName("메뉴 목록을 조회 한다.")
    public void getMenus() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.get(MENU_API_URL_TEMPLATE);

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].id").exists())
            .andExpect(jsonPath("$.[*].name").exists())
            .andExpect(jsonPath("$.[*].price").exists())
            .andExpect(jsonPath("$.[*].menuGroupId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].seq").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].menuId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].productId").exists())
            .andExpect(jsonPath("$.[*].menuProducts[*].quantity").exists());
    }

    /**
     * @Given 2개의 상품을 생성하고
     * @Given 1개의 메뉴 그룹을 생성한다.
     * @When 메뉴그룹과 상품 정보를 이용하여 메뉴를 생성한다.
     * @Then 메뉴그룹과 상품 정보를 표현한 메뉴가 추가된다.
     */
    @Test
    @DisplayName("메뉴를 추가한다.")
    public void createMenu() throws Exception {
        // Given
        Product savedFirstProduct = as(mockMvcUtil.post(상품_생성_요청()), Product.class);
        Product savedSecondProduct = as(mockMvcUtil.post(상품_생성_요청()), Product.class);
        final MenuGroup savedMenuGroup = as(mockMvcUtil.post(메뉴_그룹_생성_요청()), MenuGroup.class);

        // When
        ResultActions resultActions = mockMvcUtil.post(메뉴_생성_요청(savedMenuGroup, savedFirstProduct, savedSecondProduct));

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.menuGroupId").value(savedMenuGroup.getId()))
            .andExpect(jsonPath("$.menuProducts[*].seq").exists())
            .andExpect(jsonPath("$.menuProducts[*].menuId").exists())
            .andExpect(jsonPath("$.menuProducts[*].productId").exists())
            .andExpect(jsonPath("$.menuProducts[*].quantity").exists());

        Menu createMenuResponse = as(resultActions, Menu.class);

        assertThat(createMenuResponse.getMenuProducts())
            .extracting(MenuProduct::getProductId)
            .containsExactly(savedFirstProduct.getId(), savedSecondProduct.getId());
    }
}
