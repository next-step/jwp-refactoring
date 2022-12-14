package kitchenpos.table;

import static kitchenpos.table.TableFixture.주문_테이블_추가;
import static kitchenpos.table.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.SQLException;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 일번;
    private OrderTable 이번;
    private OrderTable 삼번;

    /*
    Feature: 단체 지정하고 해제 합니다.
        Background
            Given 주문 테이블 일번 등록
            And 주문 테이블 이번 등록
            And 주문 테이블 삼번 등록
        Scenario: 단체 지정 후 해제 합니다
            When 주문 테이블 일번 이번 단체 지정
            Then 주문 테이블 일번 이번 단체 지정 됨
            When 주문 테이블 이번 삼번 단체 지정
            Then 주문 테이블 이번 삼번 단체 지정 실패
            When 주문 테이블 일번 이번 해제
            Then 주문 테이블 일번 이번 해제됨
     */
    @BeforeEach
    public void setUp() throws SQLException {
        super.setUp();
        일번 = 주문_테이블_추가(null, 0, true).as(OrderTable.class);
        이번 = 주문_테이블_추가(null, 0, true).as(OrderTable.class);
        삼번 = 주문_테이블_추가(null, 0, true).as(OrderTable.class);
    }

    @Test
    void 단체_지정_후_해제() {
        //when
        ExtractableResponse<Response> groupResponseOneAndTwo = 단체_지정(Arrays.asList(일번, 이번));
        //then
        assertThat(groupResponseOneAndTwo.statusCode())
            .isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse<Response> groupResponseTwoAndThree = 단체_지정(Arrays.asList(이번, 삼번));
        //then
        assertThat(groupResponseTwoAndThree.statusCode())
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        //when
        TableGroup tableGroup = groupResponseOneAndTwo.as(TableGroup.class);
        ExtractableResponse<Response> ungroupResponse = TableGroupFixture
            .단체_지정_해제(tableGroup.getId());
        //then
        assertThat(ungroupResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
