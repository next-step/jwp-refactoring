package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.exception.TableGroupException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroup tableGroup;
    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    private MenuProduct chicken_menuProduct;
    private MenuProduct ham_menuProduct;
    private OrderLineItem chickenOrder;
    private OrderLineItem hamOrder;


    @BeforeEach
    public void init() {
        setOrderTable();

        tableGroupService = new TableGroupService(eventPublisher, tableGroupRepository,
            orderTableRepository);
        when(tableGroup.getId()).thenReturn(1L);
        when(tableGroup.getCreatedDate()).thenReturn(LocalDateTime.now());
    }

    private void setOrderTable() {
        orderTable_1 = new OrderTable(0, true);
        orderTable_2 = new OrderTable(0, true);
    }


    @Test
    @DisplayName("1개 이하 테이블로 단체 테이블 생성시 에러 발생")
    void createWithUnderOneTableThrowError() {
        //given(0개의 테이블을 가진 tableGroupRequest)
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        //when & then 0개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("단체테이블은 2개 이상이여야 합니다");

        //given(1개의 테이블을 가진 tableGroupRequest)
        tableGroupRequest.setOrderTables(Arrays.asList(new OrderTableRequest()));

        //when & then 1개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("단체테이블은 2개 이상이여야 합니다");
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 단체 테이블 생성시 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(
            Arrays.asList(new OrderTableRequest(), new OrderTableRequest()));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("단체테이블은 2개 이상이여야 합니다");
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 단체 테이블 생성시 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        orderTable_1.useTable();
        when(orderTableRepository.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("이미 사용중인 테이블입니다");
    }

    @Test
    @DisplayName("단체 테이블 해체 정상로직")
    void ungroupHappyCase() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setId(1L);

        doNothing().when(eventPublisher)
            .publishEvent(any());

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupRequest.getId()));
    }

    @Test
    @DisplayName("단체 테이블 해체시 요리중이거나 먹고있는 오더가 있으면 에러가 발생한다")
    void ungroupWithCookingMealOrderThrowError() {
        //given
        doThrow(IllegalArgumentException.class).when(eventPublisher)
            .publishEvent(any());

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}