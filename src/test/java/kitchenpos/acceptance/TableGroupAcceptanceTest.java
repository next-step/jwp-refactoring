package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정_요청;
import static kitchenpos.acceptance.TableGroupRestAssured.단체_지정되어_있음;
import static kitchenpos.acceptance.TableGroupRestAssured.단체_해제_요청;
import static kitchenpos.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private TableGroup 단체A;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블A = 주문_테이블_등록되어_있음(generateOrderTable(null, 5, true)).as(OrderTable.class);
        주문테이블B = 주문_테이블_등록되어_있음(generateOrderTable(null, 4, true)).as(OrderTable.class);
        단체A = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
    }

    @DisplayName("주문 테이블들에 대해 단체를 설정한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체_지정_요청(단체A);

        // then
        단체_지정됨(response);
    }

    @DisplayName("등록된 테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = 단체_지정되어_있음(단체A).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체_해제_요청(tableGroup.getId());

        // then
        단체_해제됨(response);
    }

    private static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    private static void 단체_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
