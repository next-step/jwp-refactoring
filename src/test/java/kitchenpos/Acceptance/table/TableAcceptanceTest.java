package kitchenpos.Acceptance.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static kitchenpos.Acceptance.order.OrderAcceptanceTest.주문_상태_변경_요청;
import static kitchenpos.Acceptance.order.OrderAcceptanceTest.주문_테이블에_새로운_주문_생성;
import static kitchenpos.Acceptance.tableGroup.TableGroupAcceptanceTest.테이블_그룹_생성_요청;
import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.domain.OrderTest.주문_생성;
import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/api/tables";

    @DisplayName("테이블 생성 시 정상 생성되어야 한다")
    @Test
    void createTableTest() {
        // given
        int 테이블_인원_수 = 3;

        // when
        ExtractableResponse<Response> 테이블_생성_결과 = 테이블_생성_요청(테이블_인원_수);

        // then
        테이블_생성됨(테이블_생성_결과, 테이블_인원_수);
    }

    @DisplayName("테이블 목록 조회 시 정사 조회되어야 한다")
    @Test
    void findAllTableTest() {
        // given
        int createCount = 3;
        for (int i = 0; i < createCount; i++) {
            테이블_생성_요청(3);
        }

        // when
        ExtractableResponse<Response> 테이블_생성_결과 = 테이블_목록_조회();

        // then
        테이블_목록_조회됨(테이블_생성_결과, createCount);
    }

    @DisplayName("저장되지 않은 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void updateEmptyByBeforeSavedTableTest() {
        // given
        OrderTable 변경_될_테이블 = 주문_테이블_생성(null, 1, false);

        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_요청(-1L, 변경_될_테이블);

        // then
        빈_테이블_변경_요청_실패됨(빈_테이블_변경_결과);
    }

    @DisplayName("그룹에 속한 주문 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void updateEmptyByNotNullTableGroupTest() {
        // given
        List<OrderTable> 저장_된_테이블들 = Arrays.asList(
                테이블_생성_요청(3).as(OrderTable.class),
                테이블_생성_요청(3).as(OrderTable.class),
                테이블_생성_요청(3).as(OrderTable.class)
        );
        OrderTable 변경_될_테이블 = 주문_테이블_생성(1L, 1, false);
        테이블_그룹_생성_요청(저장_된_테이블들);

        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_요청(저장_된_테이블들.get(0).getId(), 변경_될_테이블);

        // then
        빈_테이블_변경_요청_실패됨(빈_테이블_변경_결과);
    }

    @DisplayName("요리중 또는 식사중인 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void updateEmptyByCookingOrMealStatusTableTest() {
        // given
        OrderTable 주문_테이블 = 테이블_생성_요청(3).as(OrderTable.class);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 3, false);
        빈_테이블_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);
        주문_테이블에_새로운_주문_생성(주문_테이블);

        // when
        ExtractableResponse<Response> 요리중_테이블_변경_결과 = 빈_테이블_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        빈_테이블_변경_요청_실패됨(요리중_테이블_변경_결과);

        // when
        Order 변경_될_주문 = 주문_생성(주문_테이블.getId(), OrderStatus.MEAL, Collections.emptyList());
        주문_상태_변경_요청(주문_테이블.getId(), 변경_될_주문);
        ExtractableResponse<Response> 식사중_테이블_변경_결과 = 빈_테이블_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        빈_테이블_변경_요청_실패됨(식사중_테이블_변경_결과);
    }

    @DisplayName("정상 상태의 테이블의 빈 테이블 여부를 변경하면 정상 동작해야 한다")
    @Test
    void updateEmptyTableTest() {
        // given
        OrderTable 주문_테이블 = 테이블_생성_요청(3).as(OrderTable.class);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 3, false);

        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        빈_테이블_변경_요청_성공됨(빈_테이블_변경_결과, 변경_될_주문_테이블.isEmpty());
    }

    @DisplayName("변경하는 좌석수가 음수인 경우 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByMinusTest() {
        // given
        OrderTable 주문_테이블 = 테이블_생성_요청(3).as(OrderTable.class);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, -1, true);

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("저장되지 않은 테이블의 좌석수를 변경하면 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByNotSavedTableTest() {
        // given
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, true);

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_요청(-1L, 변경_될_주문_테이블);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("빈 테이블의 좌석수를 변경하면 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByEmptyTableTest() {
        // given
        OrderTable 주문_테이블 = 테이블_생성_요청(3).as(OrderTable.class);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, true);
        빈_테이블_변경_요청(주문_테이블.getId(), 주문_테이블_생성(null, 3, true));

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("정상 상태의 테이블의 좌석수를 변경하면 정상 반영되어야 한다")
    @Test
    void updateNumberOfGuestsTest() {
        // given
        OrderTable 주문_테이블 = 테이블_생성_요청(3).as(OrderTable.class);
        OrderTable 변경_될_주문_테이블 = 주문_테이블_생성(null, 10, true);
        빈_테이블_변경_요청(주문_테이블.getId(), 주문_테이블_생성(null, 3, false));

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_요청(주문_테이블.getId(), 변경_될_주문_테이블);

        // then
        좌석수_변경_요청_성공됨(테이블_좌석수_변경_결과, 변경_될_주문_테이블.getNumberOfGuests());
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests) {
        Map<String, Object> body = new HashMap<>();

        body.put("numberOfGuests", numberOfGuests);
        body.put("empty", true);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 빈_테이블_변경_요청(Long orderTableId, OrderTable target) {
        return RestAssuredRequest.putRequest(PATH + "/{orderTableId}/empty", Collections.emptyMap(), target, orderTableId);
    }

    public static ExtractableResponse<Response> 테이블_좌석수_변경_요청(Long orderTableId, OrderTable target) {
        return RestAssuredRequest.putRequest(PATH + "/{orderTableId}/number-of-guests", Collections.emptyMap(), target, orderTableId);
    }

    void 테이블_생성됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        OrderTable orderTable = response.as(OrderTable.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
        assertThat(orderTable.isEmpty()).isTrue();
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    void 테이블_목록_조회됨(ExtractableResponse<Response> response, int expectedMinimum) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.body().jsonPath().getList("id", Long.class).size()).isGreaterThanOrEqualTo(expectedMinimum);
    }

    void 빈_테이블_변경_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 빈_테이블_변경_요청_성공됨(ExtractableResponse<Response> response, boolean expectedEmpty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.body().jsonPath().getBoolean("empty")).isEqualTo(expectedEmpty);
    }

    void 좌석수_변경_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 좌석수_변경_요청_성공됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(response.body().jsonPath().getInt("numberOfGuests")).isEqualTo(expectedNumberOfGuests);
    }
}
