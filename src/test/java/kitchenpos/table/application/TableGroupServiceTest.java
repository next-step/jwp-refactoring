package kitchenpos.table.application;

import static common.OrderTableFixture.from;
import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static common.TableGroupFixture.단체테이블_첫번째_두번째;
import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

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
        verify(tableGroupDao, times(1)).save(any());
    }

}
