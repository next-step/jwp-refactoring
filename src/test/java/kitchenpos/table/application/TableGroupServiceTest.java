package kitchenpos.table.application;

import static common.OrderTableFixture.from;
import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static common.TableGroupFixture.단체테이블_첫번째_두번째;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
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
        // given
        TableGroup 단체테이블_첫번째_두번째 = 단체테이블_첫번째_두번째();
        OrderTable 단체지정_첫번째_주문테이블 = 단체지정_첫번째_주문테이블();
        OrderTable 단체지정_두번째_주문테이블 = 단체지정_두번째_주문테이블();
        OrderTableRequest 첫번째_테이블 = from(단체지정_첫번째_주문테이블);
        OrderTableRequest 두번째_테이블 = from(단체지정_두번째_주문테이블);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(asList(첫번째_테이블, 두번째_테이블));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(
            asList(단체지정_첫번째_주문테이블, 단체지정_두번째_주문테이블));
        when(tableGroupDao.save(any(TableGroup.class))).thenReturn(단체테이블_첫번째_두번째);

        // when
        tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupDao, atMostOnce()).save(any());
    }

    @Test
    void 단체그룹_취소() {
        // given
        OrderTable 단체지정_첫번째_주문테이블 = 단체지정_첫번째_주문테이블();
        OrderTable 단체지정_두번째_주문테이블 = 단체지정_두번째_주문테이블();
        List<OrderTable> orderTables = asList(단체지정_첫번째_주문테이블, 단체지정_두번째_주문테이블);
        TableGroup tableGroup = TableGroup.of(orderTables);
        when(tableGroupDao.findById(anyLong())).thenReturn(Optional.of(tableGroup));
        TableGroup mock = mock(TableGroup.class);

        // when
        tableGroupService.ungroup(1L);

        // then
        verify(mock, atMostOnce()).unGroup();
    }

}
