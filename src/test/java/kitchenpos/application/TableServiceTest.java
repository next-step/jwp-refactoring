package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableEntity;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import kitchenpos.repository.TableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableRepository tableRepository;

    @Mock
    OrderDao orderDao;

    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 테이블_생성_성공() {
        // given
        OrderTableEntity 테이블 = new OrderTableEntity(null, 0, false);
        given(tableRepository.save(any(OrderTableEntity.class))).willReturn(테이블);

        // when
        TableResponse saved = tableService.create(new TableRequest(테이블.getNumberOfGuests(), 테이블.getEmpty()));

        // then
        assertThat(saved).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 테이블_목록_조회() {
        // given
        OrderTableEntity 테이블1 = new OrderTableEntity(null, 0, false);
        OrderTableEntity 테이블2 = new OrderTableEntity(null, 0, false);
        given(tableRepository.findAll()).willReturn(Arrays.asList(테이블1, 테이블2));

        // when
        List<TableResponse> 주문_테이블_목록 = tableService.list();

        // then
        assertThat(주문_테이블_목록).containsExactly(TableResponse.of(테이블1), TableResponse.of(테이블2));
    }

    @DisplayName("주문 상태가 올바르지 않아 빈 테이블로 설정하는 데 실패한다")
    @Test
    void 빈_테이블로_설정_예외_잘못된_주문_상태() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문_테이블_생성(Long tableGroupId, Long tableId, boolean isEmpty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setId(tableId);
        orderTable.setEmpty(isEmpty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
