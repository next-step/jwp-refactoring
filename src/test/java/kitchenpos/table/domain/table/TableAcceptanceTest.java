package kitchenpos.table.domain.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.OrderTableDomainFixture.*;
import static kitchenpos.utils.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - 주문 테이블 관리")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest 일번_주문_테이블;
    private OrderTableRequest 이번_주문_테이블;

    private OrderTableResponse 일번_주문_테이블_등록됨;
    private OrderTableResponse 이번_주문_테이블_등록됨;


    private void 주문_테이블_등록됨(ExtractableResponse<Response> actual, OrderTableRequest orderTableRequest) {
        OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response).isNotNull();
    }

    private void 주문_테이블_방문_손님_변경됨(ExtractableResponse<Response> actual, OrderTableRequest orderTableRequest) {
        OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    private void 주문_테이블_조회됨(ExtractableResponse<Response> actual, OrderTableResponse... expected) {
        final List<Long> expectedIds = Arrays.stream(expected).map(OrderTableResponse::getId).collect(Collectors.toList());

        final List<Long> response = actual.jsonPath().getList(".", OrderTableResponse.class)
                .stream().map(OrderTableResponse::getId).collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }

    private void 주문_테이블_비우기됨(ExtractableResponse<Response> actual) {
        final OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블 조회")
    public void 주문_테이블_조회() {
        // given
        일번_주문_테이블_등록됨 = 주문_테이블_생성_요청(OrderTableRequest.of(2, false)).as(OrderTableResponse.class);
        이번_주문_테이블_등록됨 = 주문_테이블_생성_요청(OrderTableRequest.of(5, false)).as(OrderTableResponse.class);

        // when
        final ExtractableResponse<Response> actual = 주문_테이블_조회_요청();

        응답_OK(actual);
        주문_테이블_조회됨(actual, 일번_주문_테이블_등록됨, 이번_주문_테이블_등록됨);
    }


    @Nested
    @DisplayName("주문 테이블 생성")
    class CreateOrderTable {
        @Test
        @DisplayName("성공")
        public void create() {
            // given
            일번_주문_테이블 = OrderTableRequest.of(2, false);

            // when
            final ExtractableResponse<Response> actual = 주문_테이블_생성_요청(일번_주문_테이블);

            // then
            응답_CREATE(actual);
            주문_테이블_등록됨(actual, 일번_주문_테이블);
        }

        @Test
        @DisplayName("주문_테이블_비우기_요청")
        public void changeEmpty() {
            // given
            final OrderTableResponse 한식_테이블_생성_됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);

            // when
            final ExtractableResponse<Response> actual = 주문_테이블_비우기_요청(한식_테이블_생성_됨.getId());

            // then
            응답_OK(actual);
            주문_테이블_비우기됨(actual);
        }
    }

    @Nested
    @DisplayName("방문 손님 관리")
    class ChangeNumberOfGuest {
        @Test
        @DisplayName("성공")
        void change() {
            // given
            final OrderTableResponse 한식_테이블_생성_됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final OrderTableRequest 변경_요청 = OrderTableRequest.of(2, false);
            // when
            final ExtractableResponse<Response> actual = 주문_테이블_방문_손님_변경_요청(변경_요청, 한식_테이블_생성_됨.getId());

            // then
            응답_OK(actual);
            주문_테이블_방문_손님_변경됨(actual, 변경_요청);
        }

        @Test
        @DisplayName("실패 - 주문 테이블이 미존재")
        void changeFailNotExistsOrderTable() {
            // given
            final OrderTableResponse 한식_테이블_생성_됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final  OrderTableRequest 변경_요청 = OrderTableRequest.of(2, false);

            // when
            final ExtractableResponse<Response> actual = 주문_테이블_방문_손님_변경_요청(변경_요청, 999);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 손님 숫자 0 미만 오류")
        void changeFailIllegalGuestCount() {
            // given
            final OrderTableResponse 한식_테이블_생성_됨 = 주문_테이블_생성_요청(한식_테이블_요청).as(OrderTableResponse.class);
            final OrderTableRequest 변경_요청 = OrderTableRequest.of(-1, false);
            // when
            final ExtractableResponse<Response> actual = 주문_테이블_방문_손님_변경_요청(변경_요청, 한식_테이블_생성_됨.getId());

            // then
            응답_BAD_REQUEST(actual);
        }
    }
}
