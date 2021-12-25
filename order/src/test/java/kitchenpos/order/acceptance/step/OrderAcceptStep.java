package kitchenpos.order.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static kitchenpos.utils.RestAssuredUtil.put;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderAcceptStep {
    private static final String BASE_URL = "/api/orders";

    public static ExtractableResponse<Response> 주문_등록_요청(OrderRequest 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static OrderResponse 주문_등록_확인(ExtractableResponse<Response> 주문_등록_응답, OrderRequest 등록_요청_데이터) {
        OrderResponse 등록된_주문 = 주문_등록_응답.as(OrderResponse.class);

        assertAll(
                () -> assertThat(주문_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(주문_등록_응답.header("Location")).isNotBlank(),
                () -> assertThat(등록된_주문).satisfies(등록된_주문_확인(등록_요청_데이터))
        );

        return 등록된_주문;
    }

    private static Consumer<OrderResponse> 등록된_주문_확인(OrderRequest 등록_요청_데이터) {
        return orderResponse -> {
            assertThat(orderResponse.getId()).isNotNull();
            assertThat(orderResponse.getOrderStatus()).isEqualTo("COOKING");
            주문_항목_확인(orderResponse, 등록_요청_데이터);
        };
    }

    private static void 주문_항목_확인(OrderResponse 등록된_데이터, OrderRequest 등록_요청_데이터) {
        OrderLineItemResponse 주문한_주문_항목 = 등록된_데이터.getOrderLineItems().get(0);
        OrderLineItemRequest 요청한_주문_항목 = 등록_요청_데이터.getOrderLineItems().get(0);

        assertAll(
                () -> assertThat(주문한_주문_항목.getSeq()).isNotNull(),
                () -> assertThat(주문한_주문_항목.getOrderId()).isNotNull(),
                () -> assertThat(주문한_주문_항목.getMenuId()).isEqualTo(요청한_주문_항목.getMenuId()),
                () -> assertThat(주문한_주문_항목.getQuantity()).isEqualTo(요청한_주문_항목.getQuantity())
        );
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 주문_목록_조회_확인(ExtractableResponse<Response> 주문_목록_조회_응답, OrderResponse 등록된_주문) {
        List<OrderResponse> 조회된_주문_목록 = 주문_목록_조회_응답.as(new TypeRef<List<OrderResponse>>() {
        });

        assertAll(
                () -> assertThat(주문_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_주문_목록).satisfies(조회된_테이블_목록_확인(등록된_주문))
        );
    }

    private static Consumer<List<? extends OrderResponse>> 조회된_테이블_목록_확인(OrderResponse 등록된_주문) {
        return orderResponses -> {
            assertThat(orderResponses.size()).isOne();
            assertThat(orderResponses).first()
                    .satisfies(orderResponse -> {
                        assertThat(orderResponse.getOrderTableId()).isEqualTo(등록된_주문.getOrderTableId());
                        주문_항목_확인(orderResponse, 등록된_주문);
                    });
        };
    }

    private static void 주문_항목_확인(OrderResponse 등록된_데이터, OrderResponse 등록_요청_데이터) {
        OrderLineItemResponse 주문한_주문_항목 = 등록된_데이터.getOrderLineItems().get(0);
        OrderLineItemResponse 요청한_주문_항목 = 등록_요청_데이터.getOrderLineItems().get(0);

        assertAll(
                () -> assertThat(주문한_주문_항목.getSeq()).isNotNull(),
                () -> assertThat(주문한_주문_항목.getOrderId()).isNotNull(),
                () -> assertThat(주문한_주문_항목.getMenuId()).isEqualTo(요청한_주문_항목.getMenuId()),
                () -> assertThat(주문한_주문_항목.getQuantity()).isEqualTo(요청한_주문_항목.getQuantity())
        );
    }

    public static ExtractableResponse<Response> 주문_상태_변경_요청(ExtractableResponse<Response> 주문_등록_응답, ChangeOrderStatusRequest 상태_변경_요청_데이터) {
        String uri = 주문_등록_응답.header("Location");

        return put(uri + "/order-status", 상태_변경_요청_데이터);
    }

    public static void 주문_상태_변경_확인(ExtractableResponse<Response> 주문_상태_변경_응답, ChangeOrderStatusRequest 상태_변경_요청_데이터) {
        OrderResponse 변경된_테이블 = 주문_상태_변경_응답.as(OrderResponse.class);

        assertAll(
                () -> assertThat(주문_상태_변경_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(변경된_테이블).satisfies(변경된_테이블_상태_확인(상태_변경_요청_데이터))
        );
    }

    private static Consumer<OrderResponse> 변경된_테이블_상태_확인(ChangeOrderStatusRequest 상태_변경_요청_데이터) {
        return order -> assertThat(order.getOrderStatus()).isEqualTo(상태_변경_요청_데이터.getOrderStatus());
    }
}
