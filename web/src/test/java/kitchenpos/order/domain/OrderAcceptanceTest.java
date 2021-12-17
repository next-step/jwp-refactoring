package kitchenpos.order.domain;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.order.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuDomainFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuDomainFixture.후라이드_치킨_요청;
import static kitchenpos.fixture.OrderDomainFixture.*;
import static kitchenpos.fixture.OrderTableDomainFixture.주문_테이블_생성_요청;
import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블_요청;
import static kitchenpos.utils.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - 주문 관리")
class OrderAcceptanceTest extends AcceptanceTest {

    private void 주문_등록됨(ExtractableResponse<Response> actual) {
        OrderResponse response = actual.as(OrderResponse.class);
        assertThat(response).isNotNull();
    }

    private void 주문_테이블_조회됨(ExtractableResponse<Response> actual, OrderResponse... expected) {
        final List<Long> expectedIds = Arrays.stream(expected).map(OrderResponse::getId).collect(Collectors.toList());

        final List<Long> response = actual.jsonPath().getList(".", OrderResponse.class)
                .stream().map(OrderResponse::getId).collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }

    private void 주문_상태_변경됨(ExtractableResponse<Response> actual, OrderStatus orderStatus) {
        OrderResponse response = actual.as(OrderResponse.class);
        assertThat(response.getOrderStatus()).isEqualTo(orderStatus);
    }

    @Test
    @DisplayName("주문 조회")
    public void 주문_테이블_조회() {
        // given
        final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
        final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
        final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
        final OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList(주문_내역));
        final OrderResponse 주문_등록됨 = 주문_생성_요청(주문_요청).as(OrderResponse.class);

        // when
        final ExtractableResponse<Response> actual = 주문_조회_요청();

        // then
        응답_OK(actual);
        주문_테이블_조회됨(actual, 주문_등록됨);
    }


    @Nested
    @DisplayName("주문 생성")
    class CreateOrder {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
            final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
            final OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList(주문_내역));

            // when
            final ExtractableResponse<Response> actual = 주문_생성_요청(주문_요청);

            // then
            응답_CREATE(actual);
            주문_등록됨(actual);
        }

        @Test
        @DisplayName("실패 - 주문 테이블 없음")
        public void failOrderTableEmpty() {
            // given
            final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
            final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
            final OrderRequest 주문_요청 = OrderRequest.of(null, Lists.newArrayList(주문_내역));

            // when
            final ExtractableResponse<Response> actual = 주문_생성_요청(주문_요청);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 주문 내역 없음")
        public void failOrderLineItemEmpty() {
            // given
            final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
            final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
            final OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList());

            // when
            final ExtractableResponse<Response> actual = 주문_생성_요청(주문_요청);

            // then
            응답_BAD_REQUEST(actual);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경")
    class ChangeStatus {
        @Test
        @DisplayName("성공")
        public void changeOrderStatus() {
            // given
            final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
            final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
            final OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList(주문_내역));
            final OrderResponse 주문_등록됨 = 주문_생성_요청(주문_요청).as(OrderResponse.class);
            final OrderStatus 주문_상태_변경_요청 = OrderStatus.COOKING;

            // when
            final ExtractableResponse<Response> actual = 주문_상태_변경_요청(OrderRequest.from(주문_상태_변경_요청), 주문_등록됨.getId());

            // then
            응답_OK(actual);
            주문_상태_변경됨(actual, 주문_상태_변경_요청);
        }

        @Test
        @DisplayName("실패 - 이미 완료된 주문건")
        public void failOrderStatusIllegal() {
            // given
            final OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final MenuResponse 후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
            final OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
            final OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList(주문_내역));
            final OrderResponse 주문_등록됨 = 주문_생성_요청(주문_요청).as(OrderResponse.class);
            주문_상태_변경_요청(OrderRequest.from(OrderStatus.COMPLETION), 주문_등록됨.getId());

            // when
            final ExtractableResponse<Response> actual = 주문_상태_변경_요청(OrderRequest.from(OrderStatus.COOKING), 주문_등록됨.getId());

            // then
            응답_BAD_REQUEST(actual);
        }
    }

}
