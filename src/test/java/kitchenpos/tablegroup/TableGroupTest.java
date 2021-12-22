package kitchenpos.tablegroup;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정")
class TableGroupTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("단체 지정시 테이블의 개수가 2개 미만이면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableCountLessThanTwo() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }
}
