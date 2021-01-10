package kitchenpos.infra.tableGroup;

import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderTableAdapterInTableGroupTest {
    private OrderTableAdapterInTableGroup orderTableAdapterInTableGroup;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setup() {
        orderTableAdapterInTableGroup = new OrderTableAdapterInTableGroup(orderTableRepository);
    }

    @DisplayName("존재하지 않는 주문 테이블이 섞인 경우 예외 발생")
    @Test
    void cannotGroupWithNotExistOrderTableTest() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderTableRepository.countAllById(orderTableIds)).willReturn(1);

        // when, then
        assertThatThrownBy(() -> orderTableAdapterInTableGroup.canGroupTheseTables(orderTableIds))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("존재하지 않는 주문 테이블을 단체 지정할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 주문 테이블이 섞인 경우 예외 발생")
    @Test
    void cannotGroupWithNotEmptyOrderTableTest() {
        // given
        List<Long> orderTableIds = Arrays.asList(1L, 2L);
        given(orderTableRepository.countAllById(orderTableIds)).willReturn(2);
        given(orderTableRepository.countAllByIdAndEmpty(orderTableIds, false)).willReturn(1);

        // when, then
        assertThatThrownBy(() -> orderTableAdapterInTableGroup.canGroupTheseTables(orderTableIds))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("빈 주문 테이블들로만 단체 지정할 수 있습니다.");
    }
}