package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_목록_조회_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_방문_손님_수_변경_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_비어있는지_여부_변경_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_생성_요청;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 테이블 관련 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블A = generateOrderTable(null, 5, false);
        주문테이블B = generateOrderTable(null, 4, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문테이블A);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        ExtractableResponse<Response> createResponse1 = 주문_테이블_등록되어_있음(주문테이블A);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_등록되어_있음(주문테이블B);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 테이블이 비어있는지 여부를 변경한다.")
    @Test
    void changeTableToEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_등록되어_있음(주문테이블A).as(OrderTable.class);
        boolean isEmpty = orderTable.isEmpty();
        OrderTable changeOrderTable = generateOrderTable(null, orderTable.getNumberOfGuests(), !isEmpty);

        // when
        ExtractableResponse<Response> response = 주문_테이블_비어있는지_여부_변경_요청(orderTable.getId(),
                changeOrderTable);

        // then
        주문_테이블_비어있는지_여부_변경됨(response, !isEmpty);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsInTable() {
        // given
        OrderTable orderTable = 주문_테이블_등록되어_있음(주문테이블A).as(OrderTable.class);
        int expectNumberOfGuests = 15;
        OrderTable changeOrderTable = generateOrderTable(null, expectNumberOfGuests, orderTable.isEmpty());

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
