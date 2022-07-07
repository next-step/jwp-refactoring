package kitchenpos.ui;

import static kitchenpos.utils.MockMvcUtil.as;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_구성_상품_생성_요청_객체_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청_객체_생성;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성_요청;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Menu")
public class MenuRestControllerTest extends BaseTest {

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
        final ProductResponse 항정살 = as(mockMvcUtil.post(상품_생성_요청("항정살", 20_000)), ProductResponse.class);
        final ProductResponse 고추장_불고기 = as(mockMvcUtil.post(상품_생성_요청("고추장_불고기", 15_000)), ProductResponse.class);
        final MenuGroupResponse 고기만_듬뿍 = as(mockMvcUtil.post(메뉴_그룹_생성_요청("고기만_듬뿍")), MenuGroupResponse.class);
        final CreateMenuRequest 고기_더블_더블_메뉴_생성_요청 = 메뉴_생성_요청_객체_생성(
            "고기만 듬뿍",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성_요청_객체_생성(항정살, 1),
            메뉴_구성_상품_생성_요청_객체_생성(고추장_불고기, 1)
        );

        // When
        ResultActions resultActions = mockMvcUtil.post(메뉴_생성_요청(고기_더블_더블_메뉴_생성_요청));

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.menuGroupId").value(고기만_듬뿍.getId()))
            .andExpect(jsonPath("$.menuProducts[*].seq").exists())
            .andExpect(jsonPath("$.menuProducts[*].productId").exists())
            .andExpect(jsonPath("$.menuProducts[*].quantity").exists());

        MenuResponse createMenuResponse = as(resultActions, MenuResponse.class);

        assertThat(createMenuResponse.getMenuProducts())
            .extracting(MenuProductResponse::getProductId)
            .containsExactly(항정살.getId(), 고추장_불고기.getId());
    }
}
