package kitchenpos.tableGroup.event;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.application.TableGroupValidator;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableEventHandler orderTableEventHandler;


    private OrderTable 테이블1번;
    private OrderTable 테이블2번;
    private List<OrderTable> orderTables;
    private TableGroup 단체_지정;

    @BeforeEach
    void setUp() {
        테이블1번 = OrderTableFixture.생성(0,true);
        테이블2번 = OrderTableFixture.생성(0,true);
        orderTables = Arrays.asList(테이블1번, 테이블2번);
        단체_지정 = TableGroup.empty();
    }

    @DisplayName("단체 지정")
    @Test
    void group() {
        given(orderTableRepository.saveAll(any())).willReturn(orderTables);

        OrderTableGroupEvent event = OrderTableGroupEvent.from(1L, orderTables);

        orderTableEventHandler.group(event);

        assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(1L);
        assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("단체 지정 해체 할 수 있다.")
    @Test
    void ungroup() {
        List<OrderTable> 주문테이블_목록 = Arrays.asList(OrderTableFixture.단체지정_주문테이블(), OrderTableFixture.단체지정_주문테이블());
        given(orderTableRepository.findByTableGroupId(any())).willReturn(주문테이블_목록);
        OrderTableUngroupEvent event = OrderTableUngroupEvent.from(TableGroup.empty());

        orderTableEventHandler.ungroup(event);

        assertAll(
                () -> assertThat(주문테이블_목록.get(0).getTableGroupId()).isEqualTo(null),
                () -> assertThat(주문테이블_목록.get(0).getTableGroupId()).isEqualTo(null)
        );
    }
}
