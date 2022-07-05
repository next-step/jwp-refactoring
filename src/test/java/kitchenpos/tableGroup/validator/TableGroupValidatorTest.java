package kitchenpos.tableGroup.validator;

import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.TableGenerator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.table.TableGenerator.주문_테이블_목록_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    private OrderTable 주문_테이블 = spy(TableGenerator.주문_테이블_생성(손님_수_생성(10)));

    @BeforeEach
    void setUp() {
        when(주문_테이블.getId()).thenReturn(0L);
    }

    @DisplayName("요리중 또는 식사중 상태인 주문 테이블이 포함된 단체 지정을 해제 요청시 예외가 발생해야 한다")
    @Test
    void validateUngroupByNotValidOrderStatusTest() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(true);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupValidator.possibleUngroupTableGroup(테이블_그룹));
    }

    @DisplayName("요리중 또는 식사중 상태인 주문 테이블이 포함되어 있지 않은 단체 지정을 검사하면 예외가 발생하지 않아야 한다")
    @Test
    void ungroupTest() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(false);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
        OrderTables 주문_테이블_목록 = 주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블));

        TableGroup 테이블_그룹 = 테이블_그룹_생성(주문_테이블_목록);

        // then
        assertThatNoException().isThrownBy(() -> tableGroupValidator.possibleUngroupTableGroup(테이블_그룹));
    }
}
