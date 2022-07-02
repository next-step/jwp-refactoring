package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTest.테이블_그룹_가져옴;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTest.테이블_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, false);

        // then
        주문_테이블_생성_검증됨(등록된_주문_테이블);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블1 = 주문_테이블_등록되어_있음(3, false);
        ExtractableResponse<Response> 등록된_주문_테이블2 = 주문_테이블_등록되어_있음(2, false);

        // when
        ExtractableResponse<Response> 주문_테이블_목록 = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_검증됨(주문_테이블_목록);
        주문_테이블_목록_포함됨(주문_테이블_목록, Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    @DisplayName("주문 테이블 이용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, true);
        final boolean 테이블_사용중 = false;

        // when
        OrderTable 변경된_주문_테이블 = 주문_테이블_상태_수정_요청(등록된_주문_테이블, 테이블_사용중).as(OrderTable.class);

        // then
        주문_테이블_상태_검증됨(변경된_주문_테이블, 테이블_사용중);
    }

    @DisplayName("[예외] 저장되지 않은 주문 테이블의 주문 상태 변경한다.")
    @Test
    void changeEmpty_without_save() {
        // given
        OrderTable 저장_안한_주문_테이블 = new OrderTable(99999L);
        final boolean 테이블_사용중 = false;

        // when
        ExtractableResponse<Response> 변경된_주문_테이블 = 주문_테이블_상태_수정_요청(OrderTableRequest.of(저장_안한_주문_테이블), 테이블_사용중);

        // then
        주문_테이블_상태_수정_실패(변경된_주문_테이블);
    }

    @DisplayName("[예외] 테이블 그룹에 매핑된 주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty_with_mapping_with_table_group() {
        OrderTableRequest 등록된_주문_테이블1 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        OrderTableRequest 등록된_주문_테이블2 = 주문_테이블_가져옴(주문_테이블_등록되어_있음(3, true));
        List<OrderTableRequest> 등록된_주문_테이블_리스트 = Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2);

        TableGroupResponse 등록된_테이블_그룹 = 테이블_그룹_가져옴(테이블_그룹_생성_요청(new TableGroupRequest(등록된_주문_테이블_리스트)));
        OrderTableResponse 테이블_그룹과_매핑된_주문_테이블 = 등록된_테이블_그룹.getOrderTables().get(0);
        final boolean 테이블_사용중 = false;

        // when
        ExtractableResponse<Response> 변경된_주문_테이블 = 주문_테이블_상태_수정_요청(new OrderTableRequest(테이블_그룹과_매핑된_주문_테이블.getId()), 테이블_사용중);

        // then
        주문_테이블_상태_수정_실패(변경된_주문_테이블);
    }

    @DisplayName("[예외] 요리 중인 주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty_with_cooking_order_table() {
        OrderResponse 등록된_요리중인_주문 = 주문_등록되어_있음(주문_생성()).as(OrderResponse.class);
        OrderTable 등록된_요리중인_주문_테이블 = new OrderTable(등록된_요리중인_주문.getOrderTableId());
        final boolean 테이블_사용중 = false;

        // when
        ExtractableResponse<Response> 변경된_주문_테이블 = 주문_테이블_상태_수정_요청(OrderTableRequest.of(등록된_요리중인_주문_테이블), 테이블_사용중);

        // then
        주문_테이블_상태_수정_실패(변경된_주문_테이블);
    }

    @DisplayName("주문 테이블의 손님 수 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, false);
        final int 변경된_테이블_손님수 = 5;

        // when
        OrderTableResponse 변경된_주문_테이블 = 주문_테이블_인원_수정_요청(등록된_주문_테이블, 변경된_테이블_손님수).as(OrderTableResponse.class);
        
        // then
        주문_테이블_손님수_검증됨(변경된_주문_테이블, 변경된_테이블_손님수);
    }

    @DisplayName("[예외] 주문 테이블을 0명 미만의 손님 수로 변경한다.")
    @Test
    void changeNumberOfGuests_under_zero() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, false);
        final int 변경된_테이블_손님수 = -1;

        // when
        ExtractableResponse<Response> 인원_수정된_주문_테이블 = 주문_테이블_인원_수정_요청(등록된_주문_테이블, 변경된_테이블_손님수);

        // then
        주문_테이블_손님수_수정_실패(인원_수정된_주문_테이블);
    }

    @DisplayName("[예외] 저장하지 않은 주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests_with_not_saved_order_table() {
        // when
        ExtractableResponse<Response> 인원_수정된_주문_테이블 = 주문_테이블_인원_수정_요청(new OrderTable(9999L), 5);

        // then
        주문_테이블_손님수_수정_실패(인원_수정된_주문_테이블);
    }

    @DisplayName("[예외] 현재 비어 있는 주문 테이블의 손님 수 변경한다.")
    @Test
    void changeNumberOfGuests_empty() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, true);
        final int 변경된_테이블_손님수 = 5;

        // when
        ExtractableResponse<Response> 인원_수정된_주문_테이블 = 주문_테이블_인원_수정_요청(등록된_주문_테이블, 변경된_테이블_손님수);

        // then
        주문_테이블_손님수_수정_실패(인원_수정된_주문_테이블);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        return 주문_테이블_생성_요청(request);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_목록_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 주문_테이블_인원_수정_요청(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTableResponse responseOrderTable = response.as(OrderTableResponse.class);
        OrderTableRequest changedOrderTable = new OrderTableRequest(numberOfGuests, responseOrderTable.getEmpty());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", responseOrderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_인원_수정_요청(OrderTable orderTable, int numberOfGuests) {
        OrderTableRequest changedOrderTable = new OrderTableRequest(numberOfGuests, false);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_수정_요청(ExtractableResponse<Response> response, boolean isEmpty) {
        OrderTableResponse responseOrderTable = response.as(OrderTableResponse.class);
        OrderTableRequest changedOrderTable = new OrderTableRequest(3, isEmpty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrderTable)
                .when().put("/api/tables/{orderTableId}/empty", responseOrderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_수정_요청(OrderTableRequest response, boolean isEmpty) {
        OrderTableRequest request = new OrderTableRequest(isEmpty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/tables/{orderTableId}/empty", response.getId())
                .then().log().all()
                .extract();
    }

    public static OrderTableRequest 주문_테이블_가져옴(ExtractableResponse<Response> response) {
        return response.as(OrderTableRequest.class);
    }

    public static void 주문_테이블_상태_검증됨(OrderTable 변경된_주문_테이블, boolean 테이블_사용중) {
        assertThat(변경된_주문_테이블.isEmpty()).isEqualTo(테이블_사용중);
    }

    public static void 주문_테이블_손님수_검증됨(OrderTableResponse 변경된_주문_테이블, int 변경된_테이블_손님수) {
        assertThat(변경된_주문_테이블.getNumberOfGuests()).isEqualTo(변경된_테이블_손님수);
    }

    public static void 주문_테이블_상태_수정_실패(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 주문_테이블_손님수_수정_실패(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
