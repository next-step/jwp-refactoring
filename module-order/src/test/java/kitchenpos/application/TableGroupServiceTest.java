package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;
    @Mock
    ApplicationEventPublisher publisher;

    TableGroupService tableGroupService;

    TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository, publisher);
        tableGroupRequest = new TableGroupRequest(Arrays.asList(
                new OrderTableIdRequest(1L),
                new OrderTableIdRequest(2L)
        ));
    }

    @DisplayName("정상적으로 테이블 그룹 생성")
    @Test
    void 정상적으로_테이블_그룹_생성() {
        //given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

        given(tableGroupRepository.save(any())).willReturn(tableGroup);

        //when
        TableGroupResponse resultTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(resultTableGroup.getId()).isNotNull();
    }
}
