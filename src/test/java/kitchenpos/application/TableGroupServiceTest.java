package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
@DisplayName("단체 지정에 대한 비즈니스 로직")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void create() {
        // given


        // when


        // then

    }

    @DisplayName("주문 테이블이 2개 보다 작으면 생성할 수 없다.")
    @Test
    void tableSize() {
        // given


        // when


        // then

    }

    @DisplayName("주문 테이블이 주문 등록 가능 상태이면 생성할 수 없다.")
    @Test
    void notEmptyTable() {
        // given


        // when


        // then

    }

    @DisplayName("단체 지정이 되어 있으면 생성할 수 없다.")
    @Test
    void already() {
        // given


        // when


        // then

    }

}
