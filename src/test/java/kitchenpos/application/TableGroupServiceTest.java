package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTableEntity;
import kitchenpos.domain.TableGroupEntity;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("주문 테이블 단체 지정 해제에 실패한다.")
    @Test
    void 단체_지정_해제_예외() {
        // given
        OrderTableEntity 빈_테이블1 = new OrderTableEntity(null, 0, true);
        OrderTableEntity 빈_테이블2 = new OrderTableEntity(null, 0, true);
        TableGroupEntity 테이블_그룹 = new TableGroupEntity(1L, Arrays.asList(빈_테이블1, 빈_테이블2));
        doReturn(Optional.of(테이블_그룹)).when(tableGroupRepository).findById(any(Long.class));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블들의 상태가 조리 혹은 식사이기 때문에 단체 지정 해제할 수 없습니다.");
    }
}
