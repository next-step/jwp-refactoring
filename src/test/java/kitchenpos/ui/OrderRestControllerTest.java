package kitchenpos.ui;

import static kitchenpos.utils.MockMvcUtil.as;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_구성_상품_생성_요청_객체_생성;
import static kitchenpos.utils.generator.MenuFixtureGenerator.메뉴_생성_요청;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성_요청;
import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_상태_변경_요청;
import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성_요청;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성_요청;
import static kitchenpos.utils.generator.ProductFixtureGenerator.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:Order")
public class OrderRestControllerTest extends BaseTest {

    public static final String ORDER_API_BASE_URL = "/api/orders";
    public static final String UPDATE_ORDER_STATUS_API_URL_TEMPLATE = ORDER_API_BASE_URL.concat("/{orderId}/order-status");

    private ProductResponse 물냉면, 비빔냉면, 삼겹살, 항정살, 고추장_불고기;
    private MenuGroupResponse 고기랑_같이, 고기만_듬뿍;
    private MenuResponse 커플_냉삼_메뉴, 고기_더블_더블_메뉴;
    private OrderTableResponse 비어있지_않은_주문_테이블;

    /**
     * @Given 상품을 2개를 생성한다.
     * @Given 메뉴 그룹을 생성한다.
     * @Given 메뉴 그룹과 메뉴에 포함할 상품정보를 이용하여 메뉴를 생성한다.
     * @Given 위와 동일한 방법으로 메뉴를 하나 더 생성한다.
     * @Given 고객의 매장 주문을 위한 주문 테이블을 생성한다.
     */
    @BeforeEach
    void setUp() throws Exception {
        고기_더블_더블_메뉴_생성();
        커플_냉삼_메뉴_생성();
        비어있지_않은_주문_테이블 = as(mockMvcUtil.post(비어있지_않은_주문_테이블_생성_요청()), OrderTableResponse.class);
    }

    /**
     * @When 특정 주문 테이블의 주문을 등록한다.
     * @Then 전체 주문 조회 내역에서 등록한 주문을 조회할 수 있다.
     */
    @Test
    @DisplayName("주문을 등록한다.")
    public void order() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.post(주문_생성_요청(비어있지_않은_주문_테이블, 커플_냉삼_메뉴, 고기_더블_더블_메뉴));

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(비어있지_않은_주문_테이블.getId()))
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
            .andExpect(jsonPath("$.orderedTime").exists())
            .andExpect(jsonPath("$.orderLineItemResponses[*]").exists());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    public void getAllOrders() throws Exception {
        // Given
        mockMvcUtil.post(주문_생성_요청(비어있지_않은_주문_테이블, 커플_냉삼_메뉴, 고기_더블_더블_메뉴));

        // When
        ResultActions resultActions = mockMvcUtil.get(ORDER_API_BASE_URL);

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*]", hasSize(1)));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    public void updateOrderStatus() throws Exception {
        // Given
        OrderResponse savedOrder = as(mockMvcUtil.post(주문_생성_요청(비어있지_않은_주문_테이블, 커플_냉삼_메뉴, 고기_더블_더블_메뉴)), OrderResponse.class);

        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL);

        // When
        ResultActions resultActions = mockMvcUtil.put(주문_상태_변경_요청(changeOrderStatusRequest, savedOrder.getId()));

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.MEAL.name()));

        OrderResponse updatedOrder = as(resultActions, OrderResponse.class);
        assertThat(updatedOrder)
            .usingRecursiveComparison()
            .as("주문상태, 주문시간을 제외한 나머지 항목이 주문상태 변경 전과 동일한지 여부 검증")
            .ignoringFields("orderStatus", "orderedTime")
            .isEqualTo(savedOrder);
    }

    private void 고기_더블_더블_메뉴_생성() throws Exception {
        물냉면 = as(mockMvcUtil.post(상품_생성_요청("물냉면", 8_000)), ProductResponse.class);
        비빔냉면 = as(mockMvcUtil.post(상품_생성_요청("비빔냉면", 8_000)), ProductResponse.class);
        삼겹살 = as(mockMvcUtil.post(상품_생성_요청("삼겹살", 15_000)), ProductResponse.class);
        고기랑_같이 = as(mockMvcUtil.post(메뉴_그룹_생성_요청("고기랑_같이")), MenuGroupResponse.class);
        커플_냉삼_메뉴 = as(mockMvcUtil.post(메뉴_생성_요청(
            "커플_냉삼_메뉴",
            20_000,
            고기랑_같이,
            메뉴_구성_상품_생성_요청_객체_생성(물냉면, 1),
            메뉴_구성_상품_생성_요청_객체_생성(비빔냉면, 1),
            메뉴_구성_상품_생성_요청_객체_생성(삼겹살, 1)
        )), MenuResponse.class);
    }

    private void 커플_냉삼_메뉴_생성() throws Exception {
        항정살 = as(mockMvcUtil.post(상품_생성_요청("항정살", 20_000)), ProductResponse.class);
        고추장_불고기 = as(mockMvcUtil.post(상품_생성_요청("고추장_불고기", 15_000)), ProductResponse.class);
        고기만_듬뿍 = as(mockMvcUtil.post(메뉴_그룹_생성_요청("고기만_듬뿍")), MenuGroupResponse.class);

        고기_더블_더블_메뉴 = as(mockMvcUtil.post(메뉴_생성_요청(
            "고기_더블_더블_메뉴",
            30_000,
            고기만_듬뿍,
            메뉴_구성_상품_생성_요청_객체_생성(항정살, 1),
            메뉴_구성_상품_생성_요청_객체_생성(고추장_불고기, 1)
        )), MenuResponse.class);
    }
}
