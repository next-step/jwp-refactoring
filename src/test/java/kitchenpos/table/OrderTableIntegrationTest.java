package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderTableIntegrationTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableService tableService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = orderTableRepository.save(new OrderTable(3, false));
        주문_테이블_2 = orderTableRepository.save(new OrderTable(0, true));
    }


    @DisplayName("주문 테이블 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, true);

        // when
        OrderTableResponse actual = tableService.create(request);

        // then
        assertThat(actual).isNotNull()
                          .extracting(OrderTableResponse::getNumberOfGuests)
                          .isEqualTo(0);
    }

    @DisplayName("전체 주문 테이블 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        assertThat(tableService.list()).isNotEmpty().hasSizeGreaterThanOrEqualTo(2);
    }

    @DisplayName("주문 테이블 빈 상태 변경 통합 테스트")
    @Test
    void changeEmptyTest() {
        assertThat(tableService.changeEmpty(주문_테이블_2.getId(), new OrderTableChangeEmptyRequest(false)))
            .isNotNull()
            .extracting(OrderTableResponse::isEmpty)
            .isEqualTo(false);
    }

    @DisplayName("주문 테이블 손님 수 변경 통합 테스트")
    @Test
    void changeNumberOfGuestsTest() {
        assertThat(tableService.changeNumberOfGuests(주문_테이블_1.getId(),
                                                     new OrderTableChangeNumberOfGuestsRequest(10)))
            .isNotNull()
            .extracting(OrderTableResponse::getNumberOfGuests)
            .isEqualTo(10);
    }
}
