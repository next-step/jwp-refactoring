package kitchenpos.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupSaveRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static kitchenpos.menu.MenuAcceptanceTest.메뉴_등록_되어있음;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.메뉴그룹_등록되어있음;
import static kitchenpos.order.OrderAcceptanceTest.주문등록되어있음;
import static kitchenpos.table.OrderTableAcceptanceTest.주문_테이블_등록되어_있음;
import static kitchenpos.product.ProductAcceptanceTest.상품_등록되어_있음;
import static kitchenpos.menu.fixtures.MenuFixtures.메뉴등록요청;
import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.한마리메뉴그룹요청;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품등록요청;
import static kitchenpos.order.fixtures.OrderFixtures.주문등록요청;
import static kitchenpos.order.fixtures.OrderLineItemFixtures.주문정보_등록요청;
import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨요청;
import static kitchenpos.tablegroup.fixtures.TableGroupFixtures.그룹테이블_그룹요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * packageName : kitchenpos.acceptance
 * fileName : TableGroupAcceptanceTest
 * author : haedoang
 * date : 2021/12/22
 * description :
 */
@DisplayName("테이블 그룹 인수테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private Long 빈_테이블1_ID;
    private Long 빈_테이블2_ID;
    private Long 테이블3_ID;
    private Long 메뉴_ID;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        빈_테이블1_ID = 주문_테이블_등록되어_있음(주문불가_다섯명테이블요청()).getId();
        빈_테이블2_ID = 주문_테이블_등록되어_있음(주문불가_두명테이블요청()).getId();
        테이블3_ID = 주문_테이블_등록되어_있음(주문가능_두명테이블요청()).getId();

        ProductResponse 양념치킨 = 상품_등록되어_있음(양념치킨요청());
        MenuGroupResponse 한마리메뉴그룹 = 메뉴그룹_등록되어있음(한마리메뉴그룹요청());

        메뉴_ID = 메뉴_등록_되어있음(
                메뉴등록요청(
                        "양념치킨하나",
                        양념치킨.getPrice(),
                        한마리메뉴그룹.getId(),
                        Lists.newArrayList(메뉴상품등록요청(양념치킨.getId(), 1L)
                        )
                )
        ).getId();
    }

    @Test
    @DisplayName("테이블을 그룹테이블로 등록한다.")
    public void create() {
        // given
        TableGroupSaveRequest 테이블_그룹_요청 = 그룹테이블_그룹요청(newArrayList(테이블_그룹요청(빈_테이블1_ID), 테이블_그룹요청(빈_테이블2_ID)));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_요청함(테이블_그룹_요청);

        // then
        테이블_그룹_성공함(response);
    }

    @Test
    @DisplayName("테이블이 빈 테이블이 아닌 경우 그룹테이블로 변경할 수 없다.")
    public void createFailByNotEmptyTable() {
        // given
        TableGroupSaveRequest 테이블_그룹_요청 = 그룹테이블_그룹요청(newArrayList(테이블_그룹요청(테이블3_ID), 테이블_그룹요청(빈_테이블2_ID)));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_요청함(테이블_그룹_요청);

        // then
        테이블_그룹_실패함(response);
    }

    @Test
    @DisplayName("존재하지 않은 테이블인 경우 그룹테이블로 변경할 수 없다.")
    public void createFailByUnknownTableId() {
        // given
        Long 등록되지않은_테이블_ID = Long.MAX_VALUE;
        TableGroupSaveRequest 테이블_그룹_요청 = 그룹테이블_그룹요청(newArrayList(테이블_그룹요청(등록되지않은_테이블_ID), 테이블_그룹요청(빈_테이블2_ID)));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_요청함(테이블_그룹_요청);

        // then
        테이블_그룹_실패함(response);
    }

    @Test
    @DisplayName("테이블의 개수가 2개보다 작은 경우 그룹테이블로 변경할 수 없다.")
    public void createFailByTablesNumber() {
        // given
        TableGroupSaveRequest 테이블_그룹_요청 = 그룹테이블_그룹요청(newArrayList(테이블_그룹요청(빈_테이블2_ID)));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_요청함(테이블_그룹_요청);

        // then
        테이블_그룹_실패함(response);
    }

    @Test
    @DisplayName("테이블 그룹화를 해제한다.")
    public void ungroup() {
        // given
        TableGroupResponse savedGroupTable = 테이블_그룹화_되어있음(그룹테이블_그룹요청(newArrayList(테이블_그룹요청(빈_테이블1_ID), 테이블_그룹요청(빈_테이블2_ID))));

        // when
        ExtractableResponse<Response> response = 테이블_그룹화_해제_요청함(savedGroupTable.getId());

        // then
        그룹화_해제됨(response);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사중인 경우 그룹화를 해제할 수 없다.")
    public void ungroupFail() {
        // given
        TableGroupResponse savedGroupTable = 테이블_그룹화_되어있음(그룹테이블_그룹요청(newArrayList(테이블_그룹요청(빈_테이블1_ID), 테이블_그룹요청(빈_테이블2_ID))));
        주문등록되어있음(주문등록요청(빈_테이블1_ID, newArrayList(주문정보_등록요청(메뉴_ID, 1L))));

        // when
        ExtractableResponse<Response> response = 테이블_그룹화_해제_요청함(savedGroupTable.getId());

        // then
        그룹해제_실패함(response);
    }

    private ExtractableResponse<Response> 테이블_그룹화_해제_요청함(Long id) {
        return RestAssured.given().log().all()
                .when()
                .delete("/api/table-groups/{tableGroupId}", id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_요청함(TableGroupSaveRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/table-groups")
                .then().log().all().extract();
    }

    private void 테이블_그룹_성공함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 테이블_그룹_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static TableGroupResponse 테이블_그룹화_되어있음(TableGroupSaveRequest request) {
        ExtractableResponse<Response> response = 테이블_그룹_요청함(request);
        return response.jsonPath().getObject("", TableGroupResponse.class);
    }

    private void 그룹화_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 그룹해제_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
