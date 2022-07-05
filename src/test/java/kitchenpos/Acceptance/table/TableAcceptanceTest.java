package kitchenpos.Acceptance.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.OrderGenerator.*;
import static kitchenpos.table.TableGenerator.*;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성_API_호출;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {

    private static final int 테이블_손님_수 = 3;

    @DisplayName("테이블 생성 시 정상 생성되어야 한다")
    @Test
    void createTableTest() {
        // given
        OrderTableCreateRequest 주문_테이블_생성_요청 = 주문_테이블_생성_요청(테이블_손님_수);

        // when
        ExtractableResponse<Response> 테이블_생성_결과 = 테이블_생성_API_호출(주문_테이블_생성_요청);

        // then
        테이블_생성됨(테이블_생성_결과, 테이블_손님_수);
    }

    @DisplayName("테이블 목록 조회 시 정상 조회되어야 한다")
    @Test
    void findAllTableTest() {
        // given
        int 주문_테이블_생성_수 = 3;
        for (int i = 0; i < 주문_테이블_생성_수; i++) {
            테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수));
        }

        // when
        ExtractableResponse<Response> 테이블_생성_결과 = 테이블_생성_목록_조회_API_호출();

        // then
        테이블_목록_조회됨(테이블_생성_결과, 주문_테이블_생성_수);
    }

    @DisplayName("저장되지 않은 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void updateEmptyByBeforeSavedTableTest() {
        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_API_호출(-1L, false);

        // then
        빈_테이블_변경_요청_실패됨(빈_테이블_변경_결과);
    }

    @DisplayName("그룹에 속한 주문 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @Test
    void updateEmptyByNotNullTableGroupTest() {
        // given
        List<Long> 저장_된_테이블_아이디들 = Arrays.asList(
                테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class).getId(),
                테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class).getId(),
                테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class).getId()
        );
        테이블_그룹_생성_API_호출(테이블_그룹_생성_요청(저장_된_테이블_아이디들));

        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_API_호출(저장_된_테이블_아이디들.get(0), false);

        // then
        빈_테이블_변경_요청_실패됨(빈_테이블_변경_결과);
    }

    @DisplayName("요리중 또는 식사중인 테이블의 빈 테이블 여부를 변경하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL" })
    void updateEmptyByCookingOrMealStatusTableTest(OrderStatus orderStatus) {
        // given
        빈_테이블_변경_API_호출(주문_테이블_아이디, false);
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디, Collections.singletonList(주문_물품_생성_요청));
        Long 생성된_주문_아이디 = 주문_생성_API_요청(주문_생성_요청).as(OrderResponse.class).getId();
        주문_상태_변경_API_요청(생성된_주문_아이디, orderStatus);

        // when
        ExtractableResponse<Response> 요리중_테이블_변경_결과 = 빈_테이블_변경_API_호출(주문_테이블_아이디, true);

        // then
        빈_테이블_변경_요청_실패됨(요리중_테이블_변경_결과);
    }

    @DisplayName("정상 상태의 테이블의 빈 테이블 여부를 변경하면 정상 동작해야 한다")
    @Test
    void updateEmptyTableTest() {
        // given
        OrderTableResponse 주문_테이블 = 테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class);
        boolean 변경_될_상태 = false;

        // when
        ExtractableResponse<Response> 빈_테이블_변경_결과 = 빈_테이블_변경_API_호출(주문_테이블.getId(), false);

        // then
        빈_테이블_변경_요청_성공됨(빈_테이블_변경_결과, 변경_될_상태);
    }

    @DisplayName("변경하는 좌석수가 음수인 경우 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByMinusTest() {
        // given
        Long 주문_테이블_아이디 = 테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class).getId();

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_API_호출(주문_테이블_아이디, -1);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("저장되지 않은 테이블의 좌석수를 변경하면 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByNotSavedTableTest() {
        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_API_호출(-1L, 10);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("빈 테이블의 좌석수를 변경하면 예외가 발생해야 한다")
    @Test
    void updateNumberOfGuestsByEmptyTableTest() {
        // given
        Long 주문_테이블_아이디 = 테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class).getId();
        빈_테이블_변경_API_호출(주문_테이블_아이디, true);

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_API_호출(주문_테이블_아이디, 10);

        // then
        좌석수_변경_요청_실패됨(테이블_좌석수_변경_결과);
    }

    @DisplayName("정상 상태의 테이블의 좌석수를 변경하면 정상 반영되어야 한다")
    @Test
    void updateNumberOfGuestsTest() {
        // given
        int 변경_될_테이블_손님_수 = 5;
        OrderTableResponse 주문_테이블 = 테이블_생성_API_호출(주문_테이블_생성_요청(테이블_손님_수)).as(OrderTableResponse.class);
        빈_테이블_변경_API_호출(주문_테이블.getId(), false);

        // when
        ExtractableResponse<Response> 테이블_좌석수_변경_결과 = 테이블_좌석수_변경_API_호출(주문_테이블.getId(), 변경_될_테이블_손님_수);

        // then
        좌석수_변경_요청_성공됨(테이블_좌석수_변경_결과, 변경_될_테이블_손님_수);
    }

    void 테이블_생성됨(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
        OrderTableResponse orderTable = response.as(OrderTableResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
        assertThat(orderTable.isEmpty()).isTrue();
        assertThat(orderTable.getTableGroup()).isNull();
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
