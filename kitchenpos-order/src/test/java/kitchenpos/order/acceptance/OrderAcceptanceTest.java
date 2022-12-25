package kitchenpos.order.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        Long 채워진_테이블_ID = 테이블_생성됨(2, false);
        Long 빈_테이블_ID = 테이블_생성됨(2, true);
        MenuRequest menuRequest = 메뉴_요청_생성();
        MenuResponse menuResponse = 메뉴_등록됨(menuRequest);
        Long 메뉴_ID = menuResponse.getId();
        OrderLineItemRequest 주문_상품_요청 = 주문_상품_요청_생성(menuResponse, 1);

        return Stream.of(dynamicTest("주문을 생성한다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(채워진_테이블_ID, Collections.singletonList(주문_상품_요청));
                // then
                주문_정상_생성됨(response);
            }),
            dynamicTest("빈테이블은 주문을 생성 할 수 없다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(빈_테이블_ID, Collections.singletonList(주문_상품_요청));
                // then
                요청_실패됨_잘못된_요청(response);
            }),
            dynamicTest("주문테이블이 생성되지 않았으면 주문을 생성 할 수 없다.", () -> {
                // given
                Long 생성되지_않은_주문테이블_ID = Long.MAX_VALUE;
                // when
                ExtractableResponse<Response> response = 주문_생성_요청(생성되지_않은_주문테이블_ID,
                    Collections.singletonList(주문_상품_요청));
                // then
                요청_실패됨_잘못된_요청(response);
            }),
            dynamicTest("주문 목록을 조회한다.", () -> {
                // when
                ExtractableResponse<Response> response = 주문_목록_조회_요청();
                // then
                주문_목록_정상_조회됨(response, 메뉴_ID);
            }), dynamicTest("주문 상태를 변경한다.", () -> {
                // given
                Long 생성된_주문_ID = 주문_생성됨(채워진_테이블_ID, Collections.singletonList(주문_상품_요청));
                // when
                ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문_ID, OrderStatus.MEAL);
                // then
                주문_정상_변경됨(response, OrderStatus.MEAL);
            }), dynamicTest("주문상태가 현재 종료되었으면 상태를 변경할 수 없다.", () -> {
                // given
                Long 생성된_주문_ID = 주문_생성됨(채워진_테이블_ID, Collections.singletonList(주문_상품_요청));
                주문상태_변경_요청(생성된_주문_ID, OrderStatus.COMPLETION);
                // when
                ExtractableResponse<Response> response = 주문상태_변경_요청(생성된_주문_ID, OrderStatus.MEAL);
                // then
                요청_실패됨_잘못된_요청(response);
            }));
    }

    public static OrderLineItemRequest 주문_상품_요청_생성(MenuResponse menuResponse, int quantity) {
        return OrderLineItemRequest.of(menuResponse.getId(), menuResponse.getName(), menuResponse.getPrice(), quantity);
    }

    public static Long 주문_생성됨(Long orderTableId, List<OrderLineItemRequest> orderLineItemRequests) {
        return 주문_생성_요청(orderTableId, orderLineItemRequests).as(OrderResponse.class).getId();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(Long orderTableId,
        List<OrderLineItemRequest> orderLineItemRequests) {
        OrderRequest orderRequest = OrderRequest.of(orderTableId, orderLineItemRequests);
        return AcceptanceTest.post("/api/orders", orderRequest);
    }

    private ExtractableResponse<Response> 주문_목록_조회_요청() {
        return AcceptanceTest.get("/api/orders");
    }

    private ExtractableResponse<Response> 주문상태_변경_요청(Long id, OrderStatus orderStatus) {
        return AcceptanceTest.put("/api/orders/" + id + "/order-status", orderStatus);
    }

    private void 주문_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 요청_실패됨_잘못된_요청(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 주문_목록_정상_조회됨(ExtractableResponse<Response> response, Long... 메뉴아이디목록) {
        List<Long> 조회_결과_목록 = response.jsonPath()
            .getList(".", OrderResponse.class)
            .stream()
            .map(it -> it.getOrderLineItemResponses())
            .flatMap(Collection::stream)
            .map(it -> it.getMenuId())
            .collect(Collectors.toList());

        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(조회_결과_목록).containsAll(Arrays.asList(메뉴아이디목록)));
    }

    private void 주문_정상_변경됨(ExtractableResponse<Response> response, OrderStatus 주문상태) {

        assertAll(() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(OrderResponse.class).getOrderStatus()).isEqualTo(주문상태));
    }

    public static MenuRequest 메뉴_요청_생성() {
        MenuGroupRequest 세마리메뉴 = MenuGroupRequest.of("세마리메뉴");
        Long 세마리메뉴_id = 메뉴_그룹_등록됨(세마리메뉴).getId();
        ProductRequest 파닭 = ProductRequest.of("파닭", BigDecimal.valueOf(10_000));
        Long 파닭_id = 상품_등록됨(파닭).getId();
        MenuProductRequest 메뉴상품 = MenuProductRequest.of(파닭_id, 2);
        return MenuRequest.of("파닭", BigDecimal.valueOf(18_000), 세마리메뉴_id, Collections.singletonList(메뉴상품));
    }

    public static MenuGroupResponse 메뉴_그룹_등록됨(MenuGroupRequest request) {
        return 메뉴_그룹_등록_요청(request).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupRequest request) {
        return AcceptanceTest.post("/api/menu-groups", request);
    }

    public static ProductResponse 상품_등록됨(ProductRequest request) {
        return 상품_등록_요청(request).as(ProductResponse.class);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest request) {
        return AcceptanceTest.post("/api/products", request);
    }

    public static MenuResponse 메뉴_등록됨(MenuRequest request) {
        return 메뉴_등록_요청(request).as(MenuResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest request) {
        return AcceptanceTest.post("/api/menus", request);
    }

}
