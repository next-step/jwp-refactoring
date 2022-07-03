package kitchenpos.ordertable.acceptance;

import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_등록_요청;
import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_목록_조회_요청;
import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_비어있는지여부_변경_요청;
import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_손님수_변경_요청;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {
    private OrderTableRequest 주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블 = createOrderTableRequest(2, false);
    }

    /**
     * When 주문테이블을 등록 요청하면
     * Then 주문테이블이 등록 됨
     */
    @DisplayName("주문테이블을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 주문테이블_등록_요청(주문테이블);

        // then
        주문테이블_등록됨(response);
    }

    /**
     * Given 주문테이블을 등록하고
     * When 주문테이블을 조회 하면
     * Then 주문테이블 목록 조회 됨
     */
    @DisplayName("주문테이블 목록을 조회 한다.")
    @Test
    void lists() {
        // given
        OrderTableResponse 등록한_주문테이블 = 주문테이블_등록_요청(주문테이블).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_목록_조회_요청();

        // then
        주문테이블_목록_조회됨(response, Lists.newArrayList(등록한_주문테이블));
    }

    /**
     * Given 주문테이블을 등록하고
     * When 주문테이블을 비어있는지 여부를 변경 요청하면
     * Then 주문테이블이 비어있는지 여부값이 변경 됨
     */
    @DisplayName("주문테이블 비어있는지 여부값을 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse 등록한_주문테이블 = 주문테이블_등록_요청(주문테이블).as(OrderTableResponse.class);

        // when
        boolean empty = true;
        ExtractableResponse<Response> response = 주문테이블_비어있는지여부_변경_요청(등록한_주문테이블, 주문테이블, empty);

        // then
        주문테이블_비어있는지여부_변경됨(response, empty);
    }

    /**
     * Given 주문테이블을 등록하고
     * When 주문테이블을 손님수를 변경 요청하면
     * Then 주문테이블 손님수가 변경 됨
     */
    @DisplayName("주문테이블을 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse 등록한_주문테이블 = 주문테이블_등록_요청(주문테이블).as(OrderTableResponse.class);

        // when
        int numberOfGuests = 4;
        ExtractableResponse<Response> response = 주문테이블_손님수_변경_요청(등록한_주문테이블, 주문테이블, numberOfGuests);

        // then
        주문테이블_손님수_변경됨(response, numberOfGuests);
    }

    private void 주문테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문테이블_목록_조회됨(ExtractableResponse<Response> response, List<OrderTableResponse> expectedOrderTables) {
        List<OrderTableResponse> orderTables = response.jsonPath().getList(".", OrderTableResponse.class);
        Assertions.assertThat(orderTables).containsExactlyElementsOf(expectedOrderTables);
    }

    private void 주문테이블_비어있는지여부_변경됨(ExtractableResponse<Response> response, boolean empty) {
        assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(empty);
    }

    private void 주문테이블_손님수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(
                numberOfGuests);
    }
}
