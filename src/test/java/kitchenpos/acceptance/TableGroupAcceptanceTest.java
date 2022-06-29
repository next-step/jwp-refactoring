package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTest.테이블_목록_조회;
import static kitchenpos.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문_테이블_1 = 테이블_생성_요청(3, true).as(OrderTable.class);
        주문_테이블_2 = 테이블_생성_요청(5, true).as(OrderTable.class);
    }

    @DisplayName("테이블 그룹 생성에 성공한다.")
    @Test
    void createTableGroup() {
        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_생성_요청(주문_테이블_1, 주문_테이블_2);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getList("orderTables").size()).isEqualTo(2);
    }

    @DisplayName("테이블 그룹 생성 시 요청한 주문 테이블 수가 2개 미만이면 실패한다.")
    @Test
    void createTableGroupFailedWhenOrderTableLessThanTwo() {
        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_생성_요청(주문_테이블_1);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 그룹 생성 시 요청한 주문 테이블 중 저장되지 않은 것이 존재하면 실패한다.")
    @Test
    void createTableGroupFailedWhenOrderTableNotSaved() {
        //given
        final OrderTable 저장되지_않은_주문_테이블 = new OrderTable(100L, 1L, 3, true);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_생성_요청(주문_테이블_1, 저장되지_않은_주문_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 그룹 생성 시 요청한 주문 테이블이 비어있지 않으면 실패한다.")
    @Test
    void createTableGroupFailedWhenOrderTableNotEmpty() {
        //given
        final OrderTable 비어있지_않은_테이블 = 테이블_생성_요청(5, false).as(OrderTable.class);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_생성_요청(주문_테이블_1, 비어있지_않은_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 그룹 생성 시 요청한 주문 테이블이 이미 그룹핑 되었으면 실패한다.")
    @Test
    void createTableGroupFailedWhenOrderTableAlreadyGrouped() {
        //given
        테이블_그룹_생성_요청(주문_테이블_1, 주문_테이블_2);
        final OrderTable[] 테이블_목록 = 테이블_목록_조회().as(OrderTable[].class);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_생성_요청(테이블_목록);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 그룹 해제 요청한다.")
    @Test
    void ungroupTableGroup() {
        //given
        final TableGroup 생성된_테이블_그룹 = 테이블_그룹_생성_요청(주문_테이블_1, 주문_테이블_2).as(TableGroup.class);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_그룹_해제_요청(생성된_테이블_그룹.getId());

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(final OrderTable... orderTables) {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(final Long tableGroupId) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("tableGroupId", tableGroupId)
                .when().delete("/api/table-groups/{tableGroupId}")
                .then().log().all()
                .extract();
    }
}
