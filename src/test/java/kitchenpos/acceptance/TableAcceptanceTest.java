package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.TableAcceptanceStep.*;
import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;

@DisplayName("주문 테이블 관련 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = createOrderTable(null, 2, false);
        주문테이블2 = createOrderTable(null, 3, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문테이블1);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<ExtractableResponse<Response>> orderTables
                = Arrays.asList(등록된_주문_테이블(주문테이블1), 등록된_주문_테이블(주문테이블2));

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_포함됨(response, orderTables);
    }

    @DisplayName("주문 테이블이 비어있는지 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = 등록된_주문_테이블(주문테이블1).as(OrderTable.class);
        boolean isEmpty = orderTable.isEmpty();
        OrderTable changeOrderTable = createOrderTable(null, orderTable.getNumberOfGuests(), !isEmpty);

        // when
        ExtractableResponse<Response> response = 주문_테이블_빈좌석_상태_변경_요청(orderTable.getId(),
                changeOrderTable);

        // then
        주문_테이블_빈좌석_여부_변경됨(response, !isEmpty);
    }

    @DisplayName("주문 테이블의 방문 고객 인원을 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = 등록된_주문_테이블(주문테이블1).as(OrderTable.class);
        int numberOfGuests = 10;
        OrderTable changeOrderTable = createOrderTable(null, numberOfGuests, orderTable.isEmpty());

        // when
        ExtractableResponse<Response> response = 주문_테이블_방문고객_인원_변경_요청(orderTable.getId(), changeOrderTable);

        // then
        주문_테이블_방문고객_인원_변경됨(response, numberOfGuests);
    }
}
