package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.rest.TableRestAssured;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    private List<OrderTableResponse> 주문_테이블_목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_목록 = Arrays.asList(
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(1, true)),
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(2, true)),
                TableRestAssured.주문_테이블_등록됨(new OrderTableCreateRequest(3, true))
        );
    }

    @Test
    void 테이블_그룹_등록_요청시_요청에_성공한다() {
        // when
//        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_지정_요청(주문_테이블_목록);

        // then
//        주문_테이블_그룹_생성됨(response);
    }

    @Test
    void 테이블_그룹_해지_요청시_요청에_성공한다() {
        // given
//        TableGroup 기존_테이블_그룹 = TableGroupRestAssured.주문_테이블_그룹_지정_요청(주문_테이블_목록).as(TableGroup.class);

        // when
//        ExtractableResponse<Response> response = TableGroupRestAssured.주문_테이블_그룹_해지_요청(기존_테이블_그룹.getId());

        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 주문_테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        TableGroup tableGroup = response.as(TableGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(tableGroup.getId()).isNotNull()
        );
    }
}
