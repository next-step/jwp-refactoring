package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableResponse;
import org.assertj.core.api.Assertions;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    TableRepository tableRepository;

    @Mock
    private OrderRepository orderRepository;


    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        OrderTable 테이블1 = new OrderTable(null, 0, false);
        OrderTable 테이블2 = new OrderTable(null, 0, false);
        given(tableRepository.findAll()).willReturn(Arrays.asList(테이블1, 테이블2));

        // when
        List<TableResponse> 주문_테이블_목록 = tableService.list();

        // then
        Assertions.assertThat(주문_테이블_목록).containsExactly(TableResponse.of(테이블1), TableResponse.of(테이블2));
    }

    @DisplayName("어떤 테이블의 주문 상태가 '조리'나 '식사'면 빈 테이블로 설정하는 데 실패한다.")
    @Test
    void 빈_테이블로_설정_예외_잘못된_주문_상태() {
        // given
        OrderTable 테이블 = new OrderTable(null, 0, false);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("조리 혹은 식사 상태인 주문이 있어서 빈 테이블로 설정할 수 없습니다. id: " + 1L);
    }
}
