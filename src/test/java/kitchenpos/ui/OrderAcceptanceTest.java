package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends OrderAcceptanceTestFixture {

    /**
     *   When 주문을 등록하면
     *   Then 주문이 등록된다
     */
    @DisplayName("주문을 등록한다")
    @Test
    void 주문_등록() {
        // When
        ExtractableResponse<Response> response = 주문_등록_요청(주문);

        // Then
        주문_등록됨(response);

        // Then
        OrderResponse 등록된_주문 = 주문정보(response);
        assertAll(
                () -> assertThat(등록된_주문.getId()).isNotNull(),
                () -> assertThat(등록된_주문.getOrderedTime()).isNotNull(),
                () -> assertThat(등록된_주문.getOrderTableId()).isEqualTo(주문.getOrderTableId()),
                () -> assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    /**
     *   Given 주문을 등록하고
     *   When  주문 목록을 조회화면
     *   Then  주문 목록이 조회된다
     */
    @DisplayName("주문 목록을 조회한다")
    @Test
    void 주문_목록_조회() {
        // Given
        OrderLineItemRequest 주문_아이템_3 = new OrderLineItemRequest(떡튀순_곱배기.getId(), 2);
        List<OrderLineItemRequest> 주문_아이템_목록_2 = Arrays.asList(주문_아이템_1, 주문_아이템_3);
        OrderRequest 주문_2 = new OrderRequest(주문_테이블.getId(), OrderStatus.COOKING.name(), 주문_아이템_목록_2);
        주문_등록_되어있음(주문);
        주문_등록_되어있음(주문_2);

        // When
        ExtractableResponse<Response> response = 주문_조회_요청();

        // Then
        주문_목록_조회됨(response);

        // Then
        List<OrderResponse> 조회된_주문_목록 = 주문_목록(response);
        assertThat(조회된_주문_목록).hasSize(2);
    }

    /**
     *   Given 주문을 등록하고
     *   When  주문의 상태를 수정하면
     *   Then  주문의 상태가 수정된다
     */
    @DisplayName("주문 상태를 수정한다")
    @TestFactory
    Stream<DynamicTest> 주문_상태_수정() {
        // Given
        OrderResponse 등록된_주문 = 주문_등록_되어있음(주문);
        return Stream.of(
                dynamicTest("주문 상태를 조리에서 식사로 변경한다", () -> {
                    // When
                    // 조리 -> 식사
                    주문.setOrderStatus(OrderStatus.MEAL.name());
                    ExtractableResponse<Response> response = 주문_상태_변경_요청(등록된_주문.getId(), 주문);

                    // Then
                    주문_상태_수정됨(response);

                    // Then
                    OrderResponse 수정된_주문 = 주문정보(response);
                    assertAll(
                            () -> assertThat(수정된_주문.getId()).isEqualTo(등록된_주문.getId()),
                            () -> assertThat(수정된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
                    );
                }),
                dynamicTest("주문 상태를 식사에서 조리로 변경한다", () -> {
                    // When
                    // 식사 -> 조리
                    주문.setOrderStatus(OrderStatus.COOKING.name());
                    ExtractableResponse<Response> response = 주문_상태_변경_요청(등록된_주문.getId(), 주문);

                    // Then
                    주문_상태_수정됨(response);

                    // Then
                    OrderResponse 수정된_주문2 = 주문정보(response);
                    assertAll(
                            () -> assertThat(수정된_주문2.getId()).isEqualTo(등록된_주문.getId()),
                            () -> assertThat(수정된_주문2.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
                    );
                }),
                dynamicTest("주문 상태를 조리에서 계산 완료로 변경한다", () -> {
                    // When
                    // 조리 -> 계산 완료
                    주문.setOrderStatus(OrderStatus.COMPLETION.name());
                    ExtractableResponse<Response> response = 주문_상태_변경_요청(등록된_주문.getId(), 주문);

                    // Then
                    주문_상태_수정됨(response);

                    // Then
                    OrderResponse 수정된_주문3 = 주문정보(response);
                    assertAll(
                            () -> assertThat(수정된_주문3.getId()).isEqualTo(등록된_주문.getId()),
                            () -> assertThat(수정된_주문3.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name())
                    );
                })
        );
    }

    /**
     *   When 등록되지 않은 주문의 상태를 수정하면
     *   Then 주문 상태가 수정되지 않는다
     */
    @DisplayName("등록되지 않은 주문의 상태 수정 요청")
    @Test
    void 등록안된_주문_상태_수정() {
        // When
        주문.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> response = 주문_상태_변경_요청(1L, 주문);

        // Then
        주문_상태_수정되지_않음(response);
    }

    /**
     *   Given 주문을 등록 후 상태를 계산 완료로 수정하고
     *   When  주문의 상태를 수정 요청하면
     *   Then  주문의 상태가 수정되지 않는다
     */
    @DisplayName("계산 완료된 주문 상태 수정 요청")
    @Test
    void 계산_완료_주문_상태_수정() {
        // Given
        OrderResponse 등록된_주문 = 주문_등록_되어있음(주문);
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        주문_상태_변경_요청(등록된_주문.getId(), 주문);

        // When
        주문.setOrderStatus(OrderStatus.MEAL.name());
        ExtractableResponse<Response> response = 주문_상태_변경_요청(등록된_주문.getId(), 주문);

        // Then
        주문_상태_수정되지_않음(response);
    }
}
