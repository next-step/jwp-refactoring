package kitchenpos.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.rest.TableRestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void 신규_주문_테이블_정보가_주어진_경우_테이블_등록_요청시_요청에_성공한다() {
        // given
        int numberOfGuests = 0;
        boolean empty = true;

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_등록_요청(numberOfGuests, empty);

        // then
        주문_테이블_등록됨(response, numberOfGuests, empty);
    }

    @Test
    void 주문_테이블_목록_조회_요청시_요청에_성공한다() {
        // given
        List<OrderTable> 기존_주문_테이블_목록 = Arrays.asList(
                TableRestAssured.주문_테이블_등록됨(1).as(OrderTable.class),
                TableRestAssured.주문_테이블_등록됨(2).as(OrderTable.class)
        );

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(response, 기존_주문_테이블_목록);
    }

    @Test
    void 주문_테이블의_이용_가능_상태로_변경_요청시_요청에_성공한다() {
        // given
        OrderTable orderTable = TableRestAssured.주문_테이블_등록됨(1).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_이용_가능_상태로_변경_요청(orderTable.getId());

        // then
        주문_테이블_이용_여부_변경됨(response, true);
    }

    @Test
    void 주문_테이블을_이용_불가_상태로_변경_요청시_요청에_성공한다() {
        // given
        OrderTable orderTable = TableRestAssured.주문_테이블_등록됨(0).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_이용_불가_상태로_변경_요청(orderTable.getId());

        // then
        주문_테이블_이용_여부_변경됨(response, false);
    }

    @Test
    void 주문_테이블의_손님수_변경_요청시_요청에_성공한다() {
        // given
        OrderTable orderTable = TableRestAssured.주문_테이블_등록됨(1).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_손님수_변경_요청(orderTable.getId(), 3);

        // then
        주문_테이블_손님수_변경됨(response, 3);
    }

    private void 주문_테이블_등록됨(ExtractableResponse<Response> response, int numberOfGuests, boolean empty) {
        OrderTable 신규_주문_테이블 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(신규_주문_테이블.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(신규_주문_테이블.isEmpty()).isEqualTo(empty)
        );
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response, List<OrderTable> orderTables) {
        JsonPath jsonPath = response.jsonPath();
        List<Integer> numberOfGuestsInExpectedTables = orderTables.stream().map(OrderTable::getNumberOfGuests).collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("tableGroupId", Long.class)).isEqualTo(Arrays.asList(null, null)),
                () -> assertThat(jsonPath.getList("numberOfGuests", Integer.class)).isEqualTo(numberOfGuestsInExpectedTables)
        );
    }

    private void 주문_테이블_이용_여부_변경됨(ExtractableResponse<Response> response, boolean empty) {
        OrderTable 주문_테이블 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(주문_테이블.isEmpty()).isEqualTo(empty)
        );
    }

    private void 주문_테이블_손님수_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTable 주문_테이블 = response.as(OrderTable.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(numberOfGuests)
        );
    }
}
