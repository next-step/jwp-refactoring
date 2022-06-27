package kitchenpos.application;

import static kitchenpos.helper.ReflectionHelper.setTableGroupId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.domainService.TableTableGroupDomainService;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private TableGroupService tableGroupService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableTableGroupDomainService tableTableGroupDomainService;
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

        tableGroupService = new TableGroupService(tableTableGroupDomainService,
            tableGroupRepository);
        tableGroup = new TableGroup();
        setTableGroupId(1L, tableGroup);

    }

    private void setOrderTable() {
        orderTable_1 = new OrderTable(0, true);
        orderTable_2 = new OrderTable(0, true);
    }

    @Test
    @DisplayName("단체 테이블 생성 정상 로직")
    void createTableGroupHappyCase() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();

        doNothing().when(tableTableGroupDomainService)
            .checkOrderTableForCreateTableGroup(tableGroupRequest);
        when(tableTableGroupDomainService.saveTableGroup(tableGroupRequest)).thenReturn(tableGroup);
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        //when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertAll(
            () -> assertThat(tableGroupResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("1개 이하 테이블로 단체 테이블 생성시 에러 발생")
    void createWithUnderOneTableThrowError() {
        doThrow(IllegalArgumentException.class).when(tableTableGroupDomainService)
            .checkOrderTableForCreateTableGroup(any());

        //given(0개의 테이블을 가진 tableGroupRequest)
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        //when & then 0개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);

        //given(1개의 테이블을 가진 tableGroupRequest)
        tableGroupRequest.setOrderTables(Arrays.asList(new OrderTableRequest()));

        //when & then 1개의 테이블로 단체 테이블 생성시 에러 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 단체 테이블 생성시 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(
            Arrays.asList(new OrderTableRequest(), new OrderTableRequest()));

        doThrow(IllegalArgumentException.class).when(tableTableGroupDomainService)
            .saveTableGroup(tableGroupRequest);

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 단체 테이블 생성시 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        doThrow(IllegalArgumentException.class).when(tableTableGroupDomainService)
            .checkOrderTableForCreateTableGroup(tableGroupRequest);

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 해체 정상로직")
    void ungroupHappyCase() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setId(1L);

        doNothing().when(tableTableGroupDomainService)
            .checkAllMenuIsCompleteInTableGroup(tableGroupRequest.getId());

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupRequest.getId()));
    }

    @Test
    @DisplayName("단체 테이블 해체시 요리중이거나 먹고있는 오더가 있으면 에러가 발생한다")
    void ungroupWithCookingMealOrderThrowError() {
        //given
        doThrow(IllegalArgumentException.class).when(tableTableGroupDomainService)
            .checkAllMenuIsCompleteInTableGroup(tableGroup.getId());

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}