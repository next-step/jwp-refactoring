package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_등록되어_있음;
import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_요청함;
import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_취소_요청함;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체 지정 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable table1;
    private OrderTable table2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        table1 = OrderTable.of(null, 3, true);
        table2 = OrderTable.of(null, 2, true);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 주문 테이블 2개 이상을 단체 지정 요청함
     * Then 단체 지정이됨
     */
    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTable 주문_테이블1 = 주문_테이블_등록되어_있음(table1).as(OrderTable.class);
        OrderTable 주문_테이블2 = 주문_테이블_등록되어_있음(table2).as(OrderTable.class);

        TableGroup 단체_지정 = TableGroup.of(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
        ExtractableResponse<Response> response = 단체_지정_요청함(단체_지정);

        단체_지정이됨(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 주문 테이블 2개 미만을 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail() {
        OrderTable 주문_테이블1 = 주문_테이블_등록되어_있음(table1).as(OrderTable.class);
        TableGroup 단체_지정 = TableGroup.of(1L, Arrays.asList(주문_테이블1));

        ExtractableResponse<Response> response = 단체_지정_요청함(단체_지정);

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 등록되지 않은 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail2() {
        주문_테이블_등록되어_있음(table1);
        주문_테이블_등록되어_있음(table2);

        OrderTable 등록되지_않은_주문테이블1 = OrderTable.of(3L, 1, false);
        OrderTable 등록되지_않은_주문테이블2 = OrderTable.of(4L, 2, false);
        ExtractableResponse<Response> response =
                단체_지정_요청함(TableGroup.of(1L, Arrays.asList(등록되지_않은_주문테이블1, 등록되지_않은_주문테이블2)));

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 비어있지 않은 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail3() {
        OrderTable 주문_테이블 = 주문_테이블_등록되어_있음(table1).as(OrderTable.class);
        OrderTable 비어있지_않은_주문_테이블 = 주문_테이블_등록되어_있음(OrderTable.of(null, 2, false))
                .as(OrderTable.class);

        ExtractableResponse<Response> response =
                단체_지정_요청함(TableGroup.of(1L, Arrays.asList(주문_테이블, 비어있지_않은_주문_테이블)));

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * And 단체 지정 등록되어 있음
     * When 이미 단체 지정된 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail4() {
        OrderTable 주문_테이블1 = 주문_테이블_등록되어_있음(table1).as(OrderTable.class);
        OrderTable 주문_테이블2 = 주문_테이블_등록되어_있음(table2).as(OrderTable.class);

        단체_지정_등록되어_있음(TableGroup.of(1L, Arrays.asList(주문_테이블1, 주문_테이블2)));

        ExtractableResponse<Response> response = 단체_지정_요청함(TableGroup.of(2L, Arrays.asList(주문_테이블1, 주문_테이블2)));

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블 여러개 등록되어 있음
     * And 단체 지정 등록되어 있음
     * When 단체 지정 취소 요청함
     * Then 단체 지정 취소됨
     */
    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 주문_테이블1 = 주문_테이블_등록되어_있음(table1).as(OrderTable.class);
        OrderTable 주문_테이블2 = 주문_테이블_등록되어_있음(table2).as(OrderTable.class);

        TableGroup 단체_지정 = TableGroup.of(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
        단체_지정_등록되어_있음(단체_지정);

        ExtractableResponse<Response> response = 단체_지정_취소_요청함(단체_지정.getId());

        단체_지정_취소됨(response);
    }

    /**
     * Given 주문 테이블 등록되어 있음
     * And 단체 지정 되어 있음
     * And 주문(조리 또는 식사) 등록되어 있음
     * When 단체 지정 취소 요청함
     * Then 단체 지정 취소 실패함
     */
    @DisplayName("단체 지정된 주문 테이블들의 상태가 조리 또는 식사이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupFail() {

    }

    private void 단체_지정이됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 단체_지정_취소됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
