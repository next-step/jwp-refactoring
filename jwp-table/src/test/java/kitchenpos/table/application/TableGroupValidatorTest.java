package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.MinimumOrderTableNumberException;
import kitchenpos.table.exception.NotEmptyOrderTableGroupException;
import kitchenpos.table.fixture.TestOrderTableFactory;
import kitchenpos.table.fixture.TestTableGroupFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private TableService tableService;
    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @DisplayName("주문 테이블이 없으면 등록할 수 없다.")
    @Test
    void validateOrderTables1() {
        final TableGroupRequest 빈주문테이블_요청 = TestTableGroupFactory.테이블_그룹_요청(new ArrayList<>());

        given(tableService.findAllById(any())).willReturn(new ArrayList<>());

        assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(빈주문테이블_요청))
                .hasMessage("주문 테이블은 필수로 존재해야 합니다.");
    }

    @DisplayName("주문 테이블 하나만 등록할 수 없다.")
    @Test
    void validateOrderTables2() {
        final List<OrderTable> 주문테이블_하나_조회됨 = Collections.singletonList(TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false));
        final List<Long> 주문테이블 = Collections.singletonList(1L);
        final TableGroupRequest 주문테이블_하나_요청 = TestTableGroupFactory.테이블_그룹_요청(주문테이블);

        given(tableService.findAllById(any())).willReturn(주문테이블_하나_조회됨);

        assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(주문테이블_하나_요청))
                .isInstanceOf(MinimumOrderTableNumberException.class);
    }

    @DisplayName("빈 테이블은 등록할 수 없다.")
    @Test
    void validateOrderTables3() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, true);
        final OrderTable 빈_주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false);
        final List<OrderTable> 주문테이블_목록_조회됨 = Lists.newArrayList(주문테이블, 빈_주문테이블);
        final TableGroupRequest 주문테이블_요청 = TestTableGroupFactory.테이블_그룹_요청(Lists.newArrayList(주문테이블.getId(), 빈_주문테이블.getId()));

        given(tableService.findAllById(any())).willReturn(주문테이블_목록_조회됨);

        assertThatThrownBy(() -> tableGroupValidator.validateOrderTables(주문테이블_요청))
                .isInstanceOf(NotEmptyOrderTableGroupException.class);
    }

}
