package kitchenpos.tableGroup.application;

import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.ui.OrderTableEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @SpyBean
    OrderTableEvent orderTableDomainService;

    @MockBean
    OrderTableDao orderTableDao;

    List<OrderTableRequest> orderTableRequests;
    OrderTable 단체지정_첫번째_주문테이블;
    OrderTable 단체지정_두번째_주문테이블;
    @BeforeEach
    void setUp() {
        단체지정_두번째_주문테이블 = 단체지정_두번째_주문테이블();
        단체지정_첫번째_주문테이블 = 단체지정_첫번째_주문테이블();
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(
            단체지정_첫번째_주문테이블.getNumberOfGuests(), true);
        OrderTableRequest orderTableRequest = new OrderTableRequest(
            단체지정_두번째_주문테이블.getNumberOfGuests(), true);


        orderTableRequests = asList(orderTableRequest1, orderTableRequest);


    }

    @Test
    void 단체그룹_생성() {

        when(orderTableDao.findAllByIdIn(any())).thenReturn(asList(단체지정_첫번째_주문테이블, 단체지정_두번째_주문테이블));

        // when
        tableGroupService.create(new TableGroupRequest(orderTableRequests));

        // then
        verify(orderTableDomainService, times(1)).groupByOrderTable(any());
    }

    @Test
    void 단체그룹_취소() {
        // when
        tableGroupService.ungroup(1L);

        when(orderTableDao.findTableGroupById(any())).thenReturn(asList(단체지정_첫번째_주문테이블, 단체지정_두번째_주문테이블));

        // then
        verify(orderTableDao, times(1)).findTableGroupById(any());
    }
}
