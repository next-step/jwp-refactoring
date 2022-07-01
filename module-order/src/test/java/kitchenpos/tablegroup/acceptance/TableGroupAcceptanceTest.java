package kitchenpos.tablegroup.acceptance;

import static kitchenpos.ordertable.acceptance.TableRestAssured.주문테이블_등록_요청;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTableRequest;
import static kitchenpos.utils.DomainFixtureFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableGroupRequest 단체지정;

    @BeforeEach
    public void setUp() {
        super.setUp();

        OrderTableResponse 치킨주문테이블 = 주문테이블_등록_요청(createOrderTableRequest(2, true)).as(OrderTableResponse.class);
        OrderTableResponse 피자주문테이블 = 주문테이블_등록_요청(createOrderTableRequest(3, true)).as(OrderTableResponse.class);
        단체지정 = createTableGroupRequest(Lists.newArrayList(치킨주문테이블.getId(), 피자주문테이블.getId()));
    }

    /**
     * When 단체지정을 등록 요청하면
     * Then 단체지정이 등록 됨
     */
    @DisplayName("단체지정을 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 단체지정_등록_요청(단체지정);

        // then
        단체지정_등록됨(response);
    }

    /**
     * Given 단체지정을 등록하고
     * When 단체지정을 해제 요청하면
     * Then 단체지정이 해제 됨
     */
    @DisplayName("단체지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroupResponse 등록한_단체지정 = 단체지정_등록_요청(단체지정).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체지정_해제_요청(등록한_단체지정.getId());

        // then
        단체지정_해제됨(response);
    }

    private void 단체지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체지정_등록_요청(TableGroupRequest tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체지정_해제_요청(long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all()
                .extract();
    }
}
