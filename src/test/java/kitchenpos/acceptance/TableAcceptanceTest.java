package kitchenpos.acceptance;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.TableRestAssured.*;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 관련 인수 테스트")
public class TableAcceptanceTest extends AbstractAcceptanceTest {
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = createOrderTable(null, null, 5, false);
        주문테이블2 = createOrderTable(null, null, 4, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문테이블1);
        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문_테이블_전체_목록_조회() {
        // given
        ExtractableResponse<Response> response1 = 주문_테이블_생성_요청(주문테이블1);
        ExtractableResponse<Response> response2 = 주문_테이블_생성_요청(주문테이블2);
        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();
        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_포함됨(response, Arrays.asList(response1, response2));
    }

    @DisplayName("주문 테이블이 비어있는지 여부를 변경한다.")
    @Test
    void 주문테이블_상태_변경() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(주문테이블1).as(OrderTable.class);
        boolean isEmpty = orderTable.isEmpty();
        OrderTable changeOrderTable = createOrderTable(null, null, orderTable.getNumberOfGuests(), !isEmpty);
        // when
        ExtractableResponse<Response> response = 주문_테이블_비어있는지_여부_변경_요청(orderTable.getId(),
                changeOrderTable);
        // then
        주문_테이블_비어있는지_여부_변경됨(response, !isEmpty);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    @Test
    void 주문테이블_방문자수_변경() {
        // given
        OrderTable orderTable = 주문_테이블_생성_요청(주문테이블1).as(OrderTable.class);
        int expectNumberOfGuests = 15;
        OrderTable changeOrderTable = createOrderTable(null, null, expectNumberOfGuests, orderTable.isEmpty());
        // when
        ExtractableResponse<Response> response = 주문_테이블_방문_손님_수_변경_요청(orderTable.getId(), changeOrderTable);
        // then
        주문_테이블_방문한_손님_수_변경됨(response, expectNumberOfGuests);
    }

    private static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 주문_테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedOrderTableIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
    }

    private static void 주문_테이블_비어있는지_여부_변경됨(ExtractableResponse<Response> response, boolean isEmpty) {
        boolean actualEmpty = response.jsonPath().getBoolean("empty");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualEmpty).isEqualTo(isEmpty)
        );
    }

    private static void 주문_테이블_방문한_손님_수_변경됨(ExtractableResponse<Response> response, int expectNumberOfGuests) {
        int actualNumberOfGuests = response.jsonPath().getInt("numberOfGuests");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualNumberOfGuests).isEqualTo(expectNumberOfGuests)
        );
    }

}
