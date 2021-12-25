package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.testfixtures.TableAcceptanceFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;


public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문테이블 등록")
    @Test
    void create() {
        //given
        OrderTableRequest 첫번째_테이블 = TableAcceptanceFixtures.테이블_정의(6, false);

        //when
        ResponseEntity<OrderTableResponse> 테이블_생성_결과 = TableAcceptanceFixtures.테이블_등록_요청(첫번째_테이블);

        //then
        테이블_생성_정상_확인(테이블_생성_결과);
    }

    @Transactional
    @DisplayName("주문테이블 전체 조회")
    @Test
    void list() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();
        OrderTableResponse 두번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(3, false)).getBody();

        //when
        ResponseEntity<List<OrderTableResponse>> 조회_결과 = TableAcceptanceFixtures.테이블_전체_조회_요청();

        //then
        조회_정상(조회_결과);
        조회목록_정상(조회_결과, Arrays.asList(첫번째_테이블, 두번째_테이블));
    }

    @Transactional
    @DisplayName("주문테이블 상태 변경")
    @Test
    void changeOrderClose() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();

        //when
        OrderTableRequest 주문종료_상태로_변경 = new OrderTableRequest(true);
        ResponseEntity<OrderTableResponse> 변경_결과 = TableAcceptanceFixtures.주문테이블_상태_변경_요청(
            첫번째_테이블.getId(), 주문종료_상태로_변경);

        //then
        정상_처리_확인(변경_결과);
        상태변경_확인(변경_결과, 주문종료_상태로_변경);
    }

    @Transactional
    @DisplayName("방문자수 변경 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();

        //when
        OrderTableRequest 두명으로_변경 = new OrderTableRequest(2);
        ResponseEntity<OrderTableResponse> 변경_결과 = TableAcceptanceFixtures.주문테이블_방문자수_변경_요청(
            첫번째_테이블.getId(), 두명으로_변경);

        //then
        정상_처리_확인(변경_결과);
        방문자수변경_확인(변경_결과, 두명으로_변경);
    }

    private void 테이블_생성_정상_확인(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 조회목록_정상(ResponseEntity<List<OrderTableResponse>> response,
        List<OrderTableResponse> expectedOrderTables) {
        List<OrderTableResponse> orderTableResponses = response.getBody();
        assertThat(orderTableResponses).containsAll(expectedOrderTables);
    }

    private void 조회_정상(ResponseEntity<List<OrderTableResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 정상_처리_확인(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 상태변경_확인(ResponseEntity<OrderTableResponse> response,
        OrderTableRequest orderTableRequest) {
        OrderTableResponse orderTableResponse = response.getBody();
        assertThat(orderTableResponse.isOrderClose()).isEqualTo(orderTableRequest.isOrderClose());
    }

    private void 방문자수변경_확인(ResponseEntity<OrderTableResponse> response,
        OrderTableRequest orderTableRequest) {
        OrderTableResponse orderTableResponse = response.getBody();
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(
            orderTableRequest.getNumberOfGuests());
    }
}
