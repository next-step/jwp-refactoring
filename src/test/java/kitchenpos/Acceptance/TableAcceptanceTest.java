package kitchenpos.Acceptance;

import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_방문_손님_수_변경_요청함;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_상태_변경_요청함;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_상태_변경됨;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_생성_요청함;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_생성됨;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_손님_수_변경됨;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_요청_응답됨;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_조회_요청함;
import static kitchenpos.Acceptance.TableTestFixture.주문_테이블_조회_포함됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable EmptyOrderTable;

    @BeforeEach
    public void setUp() {
        super.setUp();
        EmptyOrderTable = new OrderTable(null, null, 0, true);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청함(EmptyOrderTable);
        //then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 빈테이블_response = 주문_테이블_생성_요청함(EmptyOrderTable);
        //when
        ExtractableResponse<Response> response = 주문_테이블_조회_요청함();
        //then
        주문_테이블_요청_응답됨(response);
        주문_테이블_조회_포함됨(response, Collections.singletonList(빈테이블_response));
    }

    @DisplayName("주문테이블 상태를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = 주문_테이블_생성_요청함(EmptyOrderTable).as(OrderTable.class);
        OrderTable requestOrderTable = new OrderTable(null, null, 5, false);
        //when
        ExtractableResponse<Response> response = 주문_테이블_상태_변경_요청함(orderTable.getId(), requestOrderTable);
        //then
        주문_테이블_요청_응답됨(response);
        주문_테이블_상태_변경됨(response, requestOrderTable);
    }



    @DisplayName("주문테이블 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = 주문_테이블_생성_요청함(EmptyOrderTable).as(OrderTable.class);
        OrderTable requestOrderTable = new OrderTable(null, null, 5, false);
        주문_테이블_상태_변경_요청함(orderTable.getId(), requestOrderTable);
        //when
        ExtractableResponse<Response> response = 주문_테이블_방문_손님_수_변경_요청함(orderTable.getId(), requestOrderTable);
        //then
        주문_테이블_요청_응답됨(response);
        주문_테이블_손님_수_변경됨(response, requestOrderTable);
    }


}
