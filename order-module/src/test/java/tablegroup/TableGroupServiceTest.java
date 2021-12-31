package tablegroup;

import kitchenpos.AcceptanceTest;
import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.TableGroupNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정 관련 기능")
class TableGroupServiceTest extends AcceptanceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void createTableGroupFailBecauseOfNotExistTable() {
        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(1L), new TableGroupCreateRequest.OrderTable(2L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(0, false));
        OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(0, false));


        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()), new TableGroupCreateRequest.OrderTable(secondOrderTable.getId()))));
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(1L,0, true));
        OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(1L, 0, true));

        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()), new TableGroupCreateRequest.OrderTable(secondOrderTable.getId()))));
        }).isInstanceOf(TableGroupNotAvailableException.class);
    }

    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 상태면 변경이 불가능하다.")
    void orderStatusCookingOrMeal() {
        // given
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        final Product product = productRepository.save(new Product("후라이드", BigDecimal.valueOf(7000)));
        final Menu menu = menuRepository.save(new Menu("후라이드 2마리", BigDecimal.valueOf(13000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
        final OrderTable firstOrderTable = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable secondOrderTable = orderTableRepository.save(new OrderTable(0, true));
        final TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(firstOrderTable.getId()), new TableGroupCreateRequest.OrderTable(secondOrderTable.getId()))));
        orderRepository.save(new Order(firstOrderTable.getId(), Arrays.asList(new OrderLineItem(menu.getId(), 1L)), OrderStatus.COOKING));
        orderRepository.save(new Order(secondOrderTable.getId(), Arrays.asList(new OrderLineItem(menu.getId(), 1L)), OrderStatus.COOKING));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.ungroup(tableGroupResponse.getId());
        });
    }

}
