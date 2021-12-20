package kitchenpos.application;

import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static common.TableGroupFixture.단체테이블_첫번째_두번째;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;


    @Test
    void 단체그룹_생성() {

        TableGroup 단체테이블_첫번째_두번째 = 단체테이블_첫번째_두번째();
        OrderTable 첫번째_테이블 = 단체지정_첫번째_주문테이블();
        OrderTable 두번째_테이블 = 단체지정_두번째_주문테이블();

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(
            asList(첫번째_테이블, 두번째_테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(단체테이블_첫번째_두번째);

        TableGroup tableGroup = tableGroupService.create(단체테이블_첫번째_두번째);

        assertThat(tableGroup).isEqualTo(단체테이블_첫번째_두번째);
    }

    @Test
    void 단체그룹_취소() {

        TableGroup 단체테이블_첫번째_두번째 = 단체테이블_첫번째_두번째();
        OrderTable 첫번째_테이블 = 단체지정_첫번째_주문테이블();
        OrderTable 두번째_테이블 = 단체지정_두번째_주문테이블();

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(asList(첫번째_테이블,
            두번째_테이블));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        tableGroupService.ungroup(단체테이블_첫번째_두번째.getId());

        verify(orderTableDao, times(2)).save(any(OrderTable.class));
    }

}
