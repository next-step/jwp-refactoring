package kitchenpos.table.application;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.table.exception.NotSupportUngroupException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.menu.fixtures.MenuFixtures;
import kitchenpos.menu.fixtures.MenuProductFixtures;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static kitchenpos.menu.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.table.fixtures.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * packageName : kitchenpos.application
 * fileName : TableGroupServiceTest
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
@DisplayName("그룹 테이블 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private Menu 메뉴;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        final Product 양념치킨 = 양념치킨();
        final MenuGroup 메뉴그룹 = 메뉴그룹("메뉴그룹");
        final MenuProduct 메뉴상품 = MenuProductFixtures.메뉴상품(양념치킨, 1L);
        메뉴 = MenuFixtures.메뉴("양념하나", 양념치킨.getPrice(), 메뉴그룹, Lists.newArrayList(메뉴상품));
    }

    @Test
    @DisplayName("테이블을 그룹화할 수 있다.")
    public void group() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문불가_다섯명테이블()), Optional.of(주문불가_두명테이블()));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(주문불가_5인_2인_그룹테이블());

        // when
        TableGroupResponse actual = tableGroupService.create(그룹테이블_그룹요청());

        // then
        assertAll(
                () -> assertThat(actual.getOrderTables()).hasSize(2),
                () -> assertThat(actual.getOrderTables()).extracting(OrderTableResponse::isEmpty).containsOnly(false)
        );
    }

    @Test
    @DisplayName("테이블 개수가 2개보다 작은 경우 등록할 수 없다.")
    public void createFailByTables() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문불가_다섯명테이블()));

        // then
        assertThatThrownBy(() -> tableGroupService.create(그룹테이블_그룹요청_예외_테이블한개())).isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블인 경우 그룹화 할 수 없다.")
    public void createFailByUnknownTable() {
        // then
        assertThatThrownBy(() -> tableGroupService.create(그룹테이블_그룹요청())).isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("테이블이 비워있지 않으면 등록할 수 없다.")
    public void createFailByUsingTable() {
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문가능_다섯명테이블()), Optional.of(주문불가_두명테이블()));

        // then
        assertThatThrownBy(() -> tableGroupService.create(그룹테이블_그룹요청())).isInstanceOf(IllegalOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹화를 해제할 수 있다.")
    public void ungroup() {
        // given
        final OrderTable 주문불가_다섯명테이블 = 주문불가_다섯명테이블();
        final OrderTable 주문불가_두명테이블 = 주문불가_두명테이블();
        List<OrderTable> orderTables = Lists.newArrayList(주문불가_다섯명테이블, 주문불가_두명테이블);
        final TableGroup tableGroup = new TableGroup(orderTables);
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(1L);

        // then
        verify(tableGroupRepository).delete(tableGroup);

    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사중인 경우 그룹화를 해제할 수 없다.")
    public void ungroupFail() {
        // given
        final OrderTable 주문불가_다섯명테이블 = 주문불가_다섯명테이블();
        final OrderTable 주문불가_두명테이블 = 주문불가_두명테이블();
        List<OrderTable> 그룹화테이블리스트 = Lists.newArrayList(주문불가_다섯명테이블, 주문불가_두명테이블);
        final TableGroup 그룹테이블 = new TableGroup(그룹화테이블리스트);

        final OrderLineItem 주문정보 = new OrderLineItem(메뉴.getId(), 1L);
        new Order(주문불가_다섯명테이블, Lists.newArrayList(주문정보));
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(그룹테이블));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(NotSupportUngroupException.class);
    }
}
