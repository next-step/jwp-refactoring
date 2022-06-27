package kitchenpos.application;

import static kitchenpos.helper.ReflectionHelper.setTableGroupId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
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
//        setMenu();
//        setOrderLineItem();

        tableGroupService = new TableGroupService(orderRepository, orderTableRepository,
            tableGroupRepository);
        tableGroup = new TableGroup();
        setTableGroupId(1L, tableGroup);

    }

    private void setOrderTable() {
        orderTable_1 = new OrderTable();
        orderTable_1.unUseTable();
        orderTable_2 = new OrderTable();
        orderTable_2.unUseTable();
    }

//    private void setMenu() {
//        Product chicken = new Product("chicken", BigDecimal.valueOf(5000));
//
//        chicken_menuProduct = new MenuProduct();
//        chicken_menuProduct.setProductId(1L);
//        chicken_menuProduct.setQuantity(1);
//        chicken_menuProduct.setMenuId(1L);
//
//        Product ham = new Product();
//        ham.setPrice(BigDecimal.valueOf(4000));
//        ham_menuProduct = new MenuProduct();
//        ham_menuProduct.setProductId(2L);
//        ham_menuProduct.setQuantity(1);
//        ham_menuProduct.setMenuId(1L);
//    }
//
//    private void setOrderLineItem() {
//        chickenOrder = new OrderLineItem();
//        chickenOrder.setMenuId(chicken_menuProduct.getMenuId());
//        chickenOrder.setQuantity(1);
//
//        hamOrder = new OrderLineItem();
//        hamOrder.setMenuId(ham_menuProduct.getMenuId());
//        hamOrder.setQuantity(2);
//    }

    @Test
    @DisplayName("단체 테이블 생성 정상 로직")
    void createTableGroupHappyCase() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();

        when(orderTableRepository.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
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

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 단체 테이블 생성시 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        when(orderTableRepository.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        orderTable_1.useTable();

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

        when(orderTableRepository.findAllByTableGroupId(tableGroupRequest.getId()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(orderTable_1.getId(), orderTable_2.getId()),
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .thenReturn(false);

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @Test
    @DisplayName("단체 테이블 해체시 요리중이거나 먹고있는 오더가 있으면 에러가 발생한다")
    void ungroupWithCookingMealOrderThrowError() {
        //given
        when(orderTableRepository.findAllByTableGroupId(tableGroup.getId()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(orderTable_1.getId(), orderTable_2.getId()),
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .thenReturn(true);

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}