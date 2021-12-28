package kitchenpos.tableGroup.application;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupRequestFixture;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.*;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private  TableService tableService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 테이블1번;
    private OrderTable 테이블2번;
    private TableGroup 단체_지정;


    @BeforeEach
    void setUp() {
        테이블1번 = OrderTableFixture.생성(0,true);
        테이블2번 = OrderTableFixture.생성(0,true);

        단체_지정 = TableGroup.empty();
    }

    @DisplayName("단체 지정을 주문 테이블 목록 으로 등록 할 수 있다.")
    @Test
    void create() {
        given(tableService.findAllByIdIn(any())).willReturn(Arrays.asList(테이블1번, 테이블2번));
        given(tableGroupRepository.save(any())).willReturn(단체_지정);

        TableGroupResponse create = tableGroupService.create(TableGroupRequestFixture.샘플_Request());

        assertAll(
                () -> assertThat(create).isNotNull()
        );
    }

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTableIdRequest 테이블요청 = new OrderTableIdRequest(1L);
        OrderTable 테이블 = OrderTableFixture.생성(0,false);
        TableGroupRequest 단체_지정_주문테이블1개_Request = TableGroupRequestFixture.생성_Request(Arrays.asList(테이블요청));
        given(tableService.findAllByIdIn(any())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정_주문테이블1개_Request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
