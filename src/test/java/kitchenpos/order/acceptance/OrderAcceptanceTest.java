package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.common.TableStatus;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ordertable.acceptance.OrderTableRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.stream.Stream;

public class OrderAcceptanceTest extends AcceptanceTest {

    private Order orderA;
    private OrderTable tableA;

    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderLineItem orderLineItemA = new OrderLineItem();
        OrderLineItem orderLineItemB = new OrderLineItem();

        orderLineItemA.setOrderId(1L);
        orderLineItemA.setMenuId(1L);
        orderLineItemA.setSeq(1L);
        orderLineItemA.setQuantity(1);

        orderLineItemB.setOrderId(2L);
        orderLineItemB.setMenuId(2L);
        orderLineItemB.setSeq(2L);
        orderLineItemB.setQuantity(2);

        orderA = OrderRestAssured.from(1L, OrderStatus.MEAL.name(), Arrays.asList(orderLineItemA, orderLineItemB));
        tableA = OrderTableRestAssured.from(1L, 0, TableStatus.USING.isTableEmpty());
    }

    @DisplayName("주문 생성 요청")
    @TestFactory
    Stream<DynamicTest> createOrder() {
        return Stream.of(
                DynamicTest.dynamicTest("empty 여부 using으로 수정", () -> {
                    ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(1L, tableA);
                    OrderTableRestAssured.테이블_정보_수정됨(response);
                }),
                DynamicTest.dynamicTest("주문생성 요청", () -> {
                    ExtractableResponse<Response> response = OrderRestAssured.주문_생성_요청(orderA);
                    OrderRestAssured.주문_생성됨(response);
                })
        );
    }

    @DisplayName("주문 상태 정보 수정 요청")
    @TestFactory
    Stream<DynamicTest> modifyOrderStatus() {
        return Stream.of(
                DynamicTest.dynamicTest("empty 여부 using으로 수정", () -> {
                    ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(1L, tableA);
                    OrderTableRestAssured.테이블_정보_수정됨(response);
                }),
                DynamicTest.dynamicTest("주문생성 요청", () -> {
                    ExtractableResponse<Response> response = OrderRestAssured.주문_생성_요청(orderA);
                    OrderRestAssured.주문_생성됨(response);
                }),
                DynamicTest.dynamicTest("주문수정 요청", () -> {
                    orderA.setOrderStatus(OrderStatus.COMPLETION.name());
                    ExtractableResponse<Response> response = OrderRestAssured.주문_수정_요청(1L, orderA);
                    OrderRestAssured.주문_수정됨(response);
                })
        );
    }

    @DisplayName("주문 상태 정보 수정 예외처리 - 완료된 주문 수정하는 경우")
    @TestFactory
    Stream<DynamicTest> makeExceptionWhenModifyOrderStatus() {
        return Stream.of(
                DynamicTest.dynamicTest("empty 여부 using으로 수정", () -> {
                    ExtractableResponse<Response> response = OrderTableRestAssured.테이블_empty_수정_요청(1L, tableA);
                    OrderTableRestAssured.테이블_정보_수정됨(response);
                }),
                DynamicTest.dynamicTest("주문생성 요청", () -> {
                    ExtractableResponse<Response> response = OrderRestAssured.주문_생성_요청(orderA);
                    OrderRestAssured.주문_생성됨(response);
                }),
                DynamicTest.dynamicTest("주문수정 요청", () -> {
                    orderA.setOrderStatus(OrderStatus.COMPLETION.name());
                    ExtractableResponse<Response> response = OrderRestAssured.주문_수정_요청(1L, orderA);
                    OrderRestAssured.주문_수정됨(response);
                }),
                DynamicTest.dynamicTest("주문수정 요청", () -> {
                    orderA.setOrderStatus(OrderStatus.MEAL.name());
                    ExtractableResponse<Response> response = OrderRestAssured.주문_수정_요청(1L, orderA);
                    OrderRestAssured.주문_수정안됨(response);
                })
        );
    }
}
