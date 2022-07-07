package kichenpos.table.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kichenpos.table.ui.dto.OrderTableCreateRequest;
import kichenpos.table.ui.dto.OrderTableCreateResponse;
import kichenpos.table.ui.dto.OrderTableUpdateRequest;
import kichenpos.table.ui.dto.OrderTableUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("주문 테이블 관리")
public class TableAcceptanceTest extends AcceptanceTest {
    public static final OrderTableCreateRequest 주문_테이블 = new OrderTableCreateRequest(0, false);
    public static final OrderTableUpdateRequest 빈_테이블 = new OrderTableUpdateRequest(0, true);
    public static final OrderTableUpdateRequest 사용중_테이블 = new OrderTableUpdateRequest(0, false);
    public static final OrderTableUpdateRequest 손님의_수_변경 = new OrderTableUpdateRequest(3, false);

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @TestFactory
    Stream<DynamicTest> 주문_테이블_관리_시나리오() {
        return Stream.of(
                dynamicTest("주문 테이블을 등록한다.", this::주문_테이블을_등록한다),
                dynamicTest("주문 테이블 목록을 조회한다.", this::주문_테이블_목록을_조회한다),
                dynamicTest("손님의 수를 변경한다", this::손님의_수를_변경한다),
                dynamicTest("테이블 이용 가능 여부를 변경한다", this::테이블_이용_가능_여부를_변경한다)
        );
    }

    private void 주문_테이블을_등록한다() {
        // when
        ExtractableResponse<Response> 주문_테이블_등록_응답 = 주문_테이블_등록_요청(주문_테이블);

        // then
        주문_테이블_등록됨(주문_테이블_등록_응답);
    }

    private void 주문_테이블_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 주문_테이블_목록_조회_응답 = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(주문_테이블_목록_조회_응답);
    }

    private void 테이블_이용_가능_여부를_변경한다() {
        // when
        ExtractableResponse<Response> 테이블_이용_가능_여부_응답 = 테이블_이용_가능_여부_변경_요청(빈_테이블);

        // then
        테이블_이용_가능_여부_변경됨(테이블_이용_가능_여부_응답);
    }

    private void 손님의_수를_변경한다() {
        // given
        테이블_이용_가능_여부_변경_요청(사용중_테이블);

        // when
        ExtractableResponse<Response> 손님_수_변경_응답 = 손님_수_변경_요청(손님의_수_변경);

        // then
        손님_수_변경됨(손님_수_변경_응답);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTableCreateRequest request) {
        return post("/api/tables", request);
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static OrderTableCreateResponse 주문_테이블_등록됨(OrderTableCreateRequest request) {
        return 주문_테이블_등록_요청(request).as(OrderTableCreateResponse.class);
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return get("/api/tables");
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(ArrayList.class)).hasSize(1);
    }

    private ExtractableResponse<Response> 손님_수_변경_요청(OrderTableUpdateRequest request) {
        return put("/api/tables/1/number-of-guests", request);
    }

    private void 손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableUpdateResponse.class).getNumberOfGuests()).isEqualTo(3);
    }

    private ExtractableResponse<Response> 테이블_이용_가능_여부_변경_요청(OrderTableUpdateRequest request) {
        return put("/api/tables/1/empty", request);
    }

    private void 테이블_이용_가능_여부_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableUpdateResponse.class).isEmpty()).isTrue();
    }
}
