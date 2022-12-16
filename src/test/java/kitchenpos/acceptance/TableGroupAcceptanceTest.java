package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.rest.TableGroupRestAssured;
import kitchenpos.rest.TableRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_목록 = Arrays.asList(
                TableRestAssured.주문_테이블_등록_요청(1, true).as(OrderTable.class),
                TableRestAssured.주문_테이블_등록_요청(2, true).as(OrderTable.class),
                TableRestAssured.주문_테이블_등록_요청(3, true).as(OrderTable.class)
        );
    }

    @Test
    void 테이블_그룹_등록_요청시_요청에_성공한다() {
        // when
        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_지정_요청(주문_테이블_목록);

        // then
        주문_테이블_그룹_생성됨(response);
    }

    @Test
    void 테이블_그룹_해지_요청시_요청에_성공한다() {
        // given
        TableGroup 기존_테이블_그룹 = TableGroupRestAssured.주문_테이블_그룹_지정_요청(주문_테이블_목록).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_해지_요청(기존_테이블_그룹.getId());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 주문_테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        TableGroup tableGroup = response.as(TableGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(tableGroup.getId()).isNotNull()
        );
    }
}
