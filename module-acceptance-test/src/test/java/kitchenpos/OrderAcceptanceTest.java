package kitchenpos;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.MenuAcceptanceUtil.신메뉴_강정치킨_가져오기;
import static kitchenpos.OrderAcceptanceTestUtil.주문_목록_응답됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_목록_조회_요청;
import static kitchenpos.OrderAcceptanceTestUtil.주문_목록_주문에_주문_항목이_포함됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_상태_변경_실패됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_상태_변경_요청;
import static kitchenpos.OrderAcceptanceTestUtil.주문_상태_변경됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_생성_실패됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_생성_요청;
import static kitchenpos.OrderAcceptanceTestUtil.주문_생성됨;
import static kitchenpos.OrderAcceptanceTestUtil.주문_생성시_조리상태_확인;
import static kitchenpos.TableAcceptanceUtil.테이블_등록됨;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블;
    private OrderTableResponse 빈_테이블;
    private MenuResponse 강정치킨;
    private OrderResponse 주문;

    @DisplayName("주문 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> order() {
        return Stream.of(
                dynamicTest("기초 데이터를 추가한다.", () -> {
                    테이블 = 테이블_등록됨(false, 5);
                    빈_테이블 = 테이블_등록됨(true, 0);
                    강정치킨 = 신메뉴_강정치킨_가져오기();
                }),
                dynamicTest("주문을 등록한다.", () -> {
                    ResponseEntity<OrderResponse> response = 주문_생성_요청(테이블, 강정치킨);

                    주문_생성됨(response);
                    주문_생성시_조리상태_확인(response);
                    주문 = response.getBody();
                }),
                dynamicTest("주문 항목 없이 주문을 등록한다.", () -> {
                    ResponseEntity<OrderResponse> response = 주문_생성_요청(테이블);

                    주문_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 메뉴가 포함된 주문 항목으로 주문을 등록한다.", () -> {
                    OrderLineItemRequest 존재하지_않는_메뉴 = new OrderLineItemRequest(Long.MAX_VALUE, 1L);

                    ResponseEntity<OrderResponse> response = 주문_생성_요청(테이블, Arrays.asList(존재하지_않는_메뉴));

                    주문_생성_실패됨(response);
                }),
                dynamicTest("빈 테이블에 주문을 등록한다.", () -> {
                    ResponseEntity<OrderResponse> response = 주문_생성_요청(빈_테이블, 강정치킨);

                    주문_생성_실패됨(response);
                }),
                dynamicTest("주문 목록을 조회한다.", () -> {
                    ResponseEntity<List<OrderResponse>> response = 주문_목록_조회_요청();

                    주문_목록_응답됨(response);
                    주문_목록_주문에_주문_항목이_포함됨(response, 강정치킨);
                }),
                dynamicTest("주문의 상태를 변경한다. (조리 -> 식사)", () -> {
                    ResponseEntity<OrderResponse> response = 주문_상태_변경_요청(주문, OrderStatus.MEAL);

                    주문_상태_변경됨(response);
                }),
                dynamicTest("주문의 상태를 변경한다. (식사 -> 계산 완료)", () -> {
                    ResponseEntity<OrderResponse> response = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    주문_상태_변경됨(response);
                }),
                dynamicTest("주문의 상태를 변경한다. (계산 완료 -> 계산 완료)", () -> {
                    ResponseEntity<OrderResponse> response = 주문_상태_변경_요청(주문, OrderStatus.COMPLETION);

                    주문_상태_변경_실패됨(response);
                }),
                dynamicTest("존재하지 않는 주문의 상태를 변경한다.", () -> {
                    Long 존재하지_않는_주문_id = Long.MAX_VALUE;

                    ResponseEntity<OrderResponse> response = 주문_상태_변경_요청(존재하지_않는_주문_id, OrderStatus.MEAL);

                    주문_상태_변경_실패됨(response);
                })
        );
    }
}
