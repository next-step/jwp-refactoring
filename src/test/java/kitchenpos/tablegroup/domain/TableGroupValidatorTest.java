package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 validation service 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableGroupValidator tableGroupValidator;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private Long 단체_지정_id;
    private TableGroup 단체_2;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = new OrderTable(1L, null, new NumberOfGuests(0), true);
        주문_테이블_2 = new OrderTable(2L, null, new NumberOfGuests(0), true);
        단체_지정_id = 1L;
        단체_2 = new TableGroup(단체_지정_id, null);
    }

    @DisplayName("2개 미만 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 주문_테이블_1개_단체_지정_예외처리() {
        assertThatThrownBy(
                () -> tableGroupValidator.getSavedOrderTablesIfValid(Arrays.asList(주문_테이블_1.getId()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 등록되지_않은_주문_테이블_예외처리() {
        List<OrderTable> 단체_지정할_주문_테이블_목록 = Arrays.asList(주문_테이블_1, 주문_테이블_2);
        List<OrderTable> 조회된_주문_테이블_목록 = Arrays.asList(주문_테이블_1);
        when(orderTableRepository.findByIdIn(주문_테이블_Id_목록(단체_지정할_주문_테이블_목록))).thenReturn(조회된_주문_테이블_목록);

        assertThatThrownBy(
                () -> tableGroupValidator.getSavedOrderTablesIfValid(주문_테이블_Id_목록(단체_지정할_주문_테이블_목록))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닌 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 비어있지_않은_주문_테이블_예외처리() {
        OrderTable 비어있지_않은_테이블 = new OrderTable(3L, null, new NumberOfGuests(0), false);
        List<Long> 비어있지_않은_주문_테이블_id_목록 = Arrays.asList(비어있지_않은_테이블.getId());

        assertThatThrownBy(
                () -> tableGroupValidator.getSavedOrderTablesIfValid(비어있지_않은_주문_테이블_id_목록)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정된 주문 테이블에 대해 단체 지정 요청 시 예외처리")
    @Test
    void 중복_단체_지정_예외처리() {
        OrderTable 단체_지정된_테이블 = new OrderTable(3L, 단체_2.getId(), new NumberOfGuests(0), false);
        List<OrderTable> 단체_지정된_주문_테이블_포함_목록 = Arrays.asList(단체_지정된_테이블, 주문_테이블_1);
        when(orderTableRepository.findByIdIn(주문_테이블_Id_목록(단체_지정된_주문_테이블_포함_목록))).thenReturn(단체_지정된_주문_테이블_포함_목록);

        assertThatThrownBy(
                () -> tableGroupValidator.getSavedOrderTablesIfValid(주문_테이블_Id_목록(단체_지정된_주문_테이블_포함_목록))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static List<Long> 주문_테이블_Id_목록(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
