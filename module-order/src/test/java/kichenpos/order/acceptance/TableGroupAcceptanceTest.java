package kichenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kichenpos.order.ui.dto.OrderTableCreateRequest;
import kichenpos.order.ui.dto.TableGroupCreateRequest;
import kichenpos.order.ui.dto.TableGroupCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Stream;

import static kichenpos.order.acceptance.TableAcceptanceTest.주문_테이블_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("단체 지정 관리")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableGroupCreateRequest 단체_지정할_테이블들;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Long 주문_테이블_id = 주문_테이블_등록됨(new OrderTableCreateRequest(3, true)).getId();
        Long 주문_테이블_id_2 = 주문_테이블_등록됨(new OrderTableCreateRequest(5, true)).getId();

        단체_지정할_테이블들 = new TableGroupCreateRequest(Arrays.asList(new OrderTableCreateRequest(주문_테이블_id), new OrderTableCreateRequest(주문_테이블_id_2)));
    }

    @TestFactory
    Stream<DynamicTest> 단체_지정_관리_시나리오() {
        return Stream.of(
                dynamicTest("단체 지정을 등록한다.", this::단체_지정을_등록한다),
                dynamicTest("단체 지정을 해제한다", this::단체_지정을_해제한다)
        );
    }

    private void 단체_지정을_등록한다() {
        // when
        ExtractableResponse<Response> 단체_지정_등록_응답 = 단체_지정_등록_요청(단체_지정할_테이블들);

        // then
        단체_지정_등록됨(단체_지정_등록_응답);
    }

    private void 단체_지정을_해제한다() {
        // when
        ExtractableResponse<Response> 단체_지정_해제_응답 = 단체_지정_해제_요청();

        // then
        단체_지정_해제됨(단체_지정_해제_응답);
    }

    public static ExtractableResponse<Response> 단체_지정_등록_요청(TableGroupCreateRequest request) {
        return post("/api/table-groups", request);
    }

    public static void 단체_지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(TableGroupCreateResponse.class).getOrderTables()).hasSizeGreaterThan(1);
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청() {
        return delete("/api/table-groups/1");
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
