package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TableRestControllerTest extends BaseTest {
    private final OrderTable 좌석 = new OrderTable(1L, 4, false);
    private final OrderTable 공석_변경_좌석 = new OrderTable(1L, 1L, 4, true);
    private final OrderTable 인원_변경_좌석 = new OrderTable(1L, 1L, 0, false);

    @Test
    void 생성() {
        ResponseEntity<OrderTable> response = 좌석_생성_요청(좌석);

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
        ResponseEntity<OrderTable> create = 좌석_생성_요청(좌석);
        HttpEntity<OrderTable> requestEntity = new HttpEntity<>(공석_변경_좌석);

        ResponseEntity<OrderTable> response = 공석_변경_요청(create.getBody().getId(), requestEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void 인원_변경() {
        ResponseEntity<OrderTable> create = 좌석_생성_요청(좌석);
        HttpEntity<OrderTable> requestEntity = new HttpEntity<>(인원_변경_좌석);

        ResponseEntity<OrderTable> response = 인원_변경_요청(create.getBody().getId(), requestEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNumberOfGuests()).isEqualTo(0);
    }

    public static ResponseEntity<OrderTable> 좌석_생성_요청(OrderTable orderTable) {
        return testRestTemplate.postForEntity(basePath + "/api/tables", orderTable, OrderTable.class);
    }

    private ResponseEntity<List<OrderTable>> 조회_요청() {
        return testRestTemplate.exchange(
                basePath + "/api/tables",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderTable>>() {});
    }

    private ResponseEntity<OrderTable> 공석_변경_요청(Long id, HttpEntity<OrderTable> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/tables/" + id + "/empty",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<OrderTable>() {});
    }

    private ResponseEntity<OrderTable> 인원_변경_요청(Long id, HttpEntity<OrderTable> requestEntity) {
        return testRestTemplate.exchange(
                basePath + "/api/tables/" + id + "/number-of-guests",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<OrderTable>() {});
    }
}
