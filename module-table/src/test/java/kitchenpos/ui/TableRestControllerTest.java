package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.OrderTableEmptyRequest;
import kitchenpos.table.dto.request.OrderTableNumberOfGuestsRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TableRestControllerTest extends BaseTest {
    private final OrderTable 좌석 = new OrderTable(null, 4, false);
    private final OrderTableEmptyRequest 공석_변경_요청 = new OrderTableEmptyRequest(true);
    private final OrderTableNumberOfGuestsRequest 인원_변경_요청 = new OrderTableNumberOfGuestsRequest(0);

    @Test
    void 생성() {
        ResponseEntity<OrderTableResponse> response = 좌석_생성_요청(좌석);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 조회() {
        좌석_생성_요청(좌석);

        ResponseEntity<List<OrderTable>> response = 조회_요청();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 공석으로_변경() {
        Long orderTableId = 좌석_생성_요청(좌석).getBody().getId();
        HttpEntity<OrderTableEmptyRequest> requestEntity = new HttpEntity<>(공석_변경_요청);

        ResponseEntity<OrderTableResponse> response = 공석_변경_요청(orderTableId, requestEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void 인원_변경() {
        ResponseEntity<OrderTableResponse> create = 좌석_생성_요청(좌석);
        HttpEntity<OrderTableNumberOfGuestsRequest> requestEntity = new HttpEntity<>(인원_변경_요청);

        ResponseEntity<OrderTableResponse> response = 인원_변경_요청(create.getBody().getId(), requestEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNumberOfGuests()).isEqualTo(0);
    }

    public static ResponseEntity<OrderTableResponse> 좌석_생성_요청(OrderTable orderTable) {
        return testRestTemplate.postForEntity(basePath + "/api/tables", orderTable, OrderTableResponse.class);
    }

    private ResponseEntity<List<OrderTable>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTable>>() {});
    }

    private ResponseEntity<OrderTableResponse> 공석_변경_요청(Long id, HttpEntity<OrderTableEmptyRequest> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/tables/" + id + "/empty",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<OrderTableResponse>() {});
    }

    private ResponseEntity<OrderTableResponse> 인원_변경_요청(Long id, HttpEntity<OrderTableNumberOfGuestsRequest> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/tables/" + id + "/number-of-guests",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<OrderTableResponse>() {});
    }
}
