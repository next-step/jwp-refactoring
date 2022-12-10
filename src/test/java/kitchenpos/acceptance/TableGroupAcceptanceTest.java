package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_요청;
import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_해제_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_생성_요청;
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

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private TableGroup 단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블A = 주문_테이블_생성_요청(new OrderTable(null, null, 4, true)).as(OrderTable.class);
        주문테이블B = 주문_테이블_생성_요청(new OrderTable(null, null, 6, true)).as(OrderTable.class);
        단체 = new TableGroup(1L, null, Arrays.asList(주문테이블A, 주문테이블B));
    }

    @DisplayName("주문 테이블의 단체를 지정한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체_지정_요청(단체);

        // then
        단체_지정됨(response);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        단체 = 단체_지정_요청(단체).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(단체.getId());

        // then
        단체_지정_해제됨(response);
    }

    private static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
