package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 비지않은_주문_테이블_1;
    private OrderTableResponse 비지않은_주문_테이블_2;
    private TableGroupResponse 테이블_그룹;

    @BeforeEach
    void setUpData() {
        비지않은_주문_테이블_1 = OrderTableAcceptanceTest
            .주문_테이블_생성_성공(OrderTableAcceptanceTest.주문_테이블_생성_요청(new OrderTableRequest(3, false)));
        비지않은_주문_테이블_2 = OrderTableAcceptanceTest
            .주문_테이블_생성_성공(OrderTableAcceptanceTest.주문_테이블_생성_요청(new OrderTableRequest(10, false)));

        TableGroupRequest request = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(비지않은_주문_테이블_1.getId()),
                          new OrderTableIdRequest(비지않은_주문_테이블_2.getId())));
        ExtractableResponse<Response> response = 주문_테이블_그룹_생성_요청(request);
        테이블_그룹 = 주문_테이블_그룹_생성_성공(response);
    }


    @DisplayName("주문 테이블 그룹 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        assertThat(테이블_그룹).isNotNull();
        assertThat(테이블_그룹.getOrderTables()
                         .stream()
                         .map(OrderTableResponse::getTableGroupId)
                         .collect(Collectors.toList()))
            .isNotEmpty()
            .doesNotContainNull();
    }

    @DisplayName("주문 테이블 그룹 해제 통합 테스트")
    @Test
    void ungroupTest() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_그룹_해제_요청(테이블_그룹.getId());

        // when
        주문_테이블_그룹_해제_성공(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_그룹_생성_요청(final TableGroupRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/api/table-groups")
                          .then().log().all().extract();
    }

    public static TableGroupResponse 주문_테이블_그룹_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 주문_테이블_그룹_해제_요청(final Long id) {
        // when
        return RestAssured.given().log().all()
                          .when().delete(String.format("/api/table-groups/%s", id))
                          .then().log().all().extract();
    }

    public static void 주문_테이블_그룹_해제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
