package kitchenpos.table.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.util.OrderTableBuilder;
import kitchenpos.table.util.TableGroupRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static kitchenpos.order.ui.OrderAcceptanceTest.*;
import static kitchenpos.table.ui.OrderTableAcceptanceTest.테이블_등록_되어_있음;
import static kitchenpos.table.ui.OrderTableAcceptanceTest.테이블_상태_변경_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private long table_1;
    private long table_2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        table_1 = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());

        table_2 = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());

    }

    @DisplayName("주문 테이블을 그룹화 할수 있다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 테이블_그룹화_요청됨(new TableGroupRequestBuilder()
                .addOrderTable(table_1)
                .addOrderTable(table_2)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블 그룹을 2개 이상이어야 한다.")
    @Test
    void tableGroupSizeGreaterThanOne() {
        // when
        ExtractableResponse<Response> response = 테이블_그룹화_요청됨(new TableGroupRequestBuilder()
                .addOrderTable(table_1)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 그룹은 빈 테이블 상태어야 한다.")
    @Test
    void tableGroupIsNotEmpty() {
        // given
        테이블_상태_변경_요청(table_1, false);

        // when
        ExtractableResponse<Response> response = 테이블_그룹화_요청됨(new TableGroupRequestBuilder()
                .addOrderTable(table_1)
                .addOrderTable(table_2)
                .build());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 테이블_그룹화_요청됨(TableGroupRequest tableGroupRequest) {
        return RestAssured.given().log().all().
                body(tableGroupRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/table-groups").
                then().
                log().all().
                extract();
    }

    @DisplayName("테이블 그룹을 삭제 할 수 있다.")
    @Test
    void deleteTableGroup() {
        // when
        long groupId = 테이블_그룹화_요청됨(new TableGroupRequestBuilder()
                .addOrderTable(table_1)
                .addOrderTable(table_2)
                .build()).as(TableGroupResponse.class).getId();

        ExtractableResponse<Response> response = 테이블_그룹_해제_요청됨(groupId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 테이블_그룹_해제_요청됨(long groupId) {
        return RestAssured.given().log().all().
                when().
                delete("/api/table-groups/{id}", groupId).
                then().
                log().all().
                extract();
    }

    @DisplayName("주문 상태가 조리, 식사중일때 단체 지정을 해제 할 수 없다.")
    @Test
    void expectedExceptionOrderStatus() {
        주문_되어있음(table_1);

        // when
        long groupId = 테이블_그룹화_요청됨(new TableGroupRequestBuilder()
                .addOrderTable(table_1)
                .addOrderTable(table_2)
                .build()).as(TableGroupResponse.class).getId();

        ExtractableResponse<Response> response = 테이블_그룹_해제_요청됨(groupId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
