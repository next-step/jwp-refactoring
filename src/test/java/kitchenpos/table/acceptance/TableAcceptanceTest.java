package kitchenpos.table.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.BaseAcceptanceTest;
import kitchenpos.table.rest.TableRestAssured;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("신규 주문 테이블 정보가 주어진 경우 테이블 등록 요청시 요청에 성공한다")
    void createOrderTableThenReturnOrderTableResponseTest() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_등록_요청(request);

        // then
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블 목록 조회 요청시 요청에_성공한다")
    void findAllOrderTablesThenReturnOrderTableResponsesTest() {
        // given
        List<OrderTableResponse> expectedOrderTables = Arrays.asList(
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, true)),
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(2, true))
        );

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_목록_조회_요청();

        // then
        JsonPath jsonPath = response.jsonPath();
        List<Integer> numberOfGuestsInExpectedTables = expectedOrderTables.stream().map(OrderTableResponse::getNumberOfGuests).collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("tableGroupId", Long.class)).isEqualTo(Arrays.asList(null, null)),
                () -> assertThat(jsonPath.getList("numberOfGuests", Integer.class)).isEqualTo(numberOfGuestsInExpectedTables)
        );
    }

    @Test
    @DisplayName("주문 테이블의 이용 가능 상태로 변경 요청시 요청에 성공한다")
    void changeOrderTableAvailableStateThenReturnResponse() {
        // given
        OrderTableResponse orderTable = TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, false));

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_이용_가능_상태로_변경_요청(orderTable.getId());

        // then
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("주문 테이블을 이용 불가 상태로 변경 요청시 요청에 성공한다")
    void changeOrderTableUnAvailableStateThenReturnResponseTest() {
        // given
        OrderTableResponse orderTable = TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, true));

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_이용_불가_상태로_변경_요청(orderTable.getId());

        // then
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTableResponse.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경 요청시 요청에 성공한다")
    void changeNumberOfGuestsThenReturnResponseTest() {
        // given
        OrderTableResponse orderTable = TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, false));

        // when
        ExtractableResponse<Response> response = TableRestAssured.주문_테이블_손님수_변경_요청(orderTable.getId(), 3);

        // then
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(3)
        );
    }
}
