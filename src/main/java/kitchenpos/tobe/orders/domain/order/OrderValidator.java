package kitchenpos.tobe.orders.domain.order;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.tobe.common.DomainService;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.menus.menu.domain.Menu;
import kitchenpos.tobe.menus.menu.domain.MenuRepository;
import kitchenpos.tobe.orders.domain.ordertable.OrderTable;
import kitchenpos.tobe.orders.domain.ordertable.OrderTableRepository;

@DomainService
public class OrderValidator implements Validator<Order> {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(
        final OrderTableRepository orderTableRepository,
        final MenuRepository menuRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(final Order order) {
        validateOrderMenu(order);
        validateOrderTable(order);
    }

    private void validateOrderMenu(final Order order) {
        final List<Long> menuIds = order.getMenuIds();
        if (menuIds.isEmpty()) {
            throw new IllegalArgumentException("1개 이상의 등록된 메뉴로 매장 주문을 등록할 수 있습니다.");
        }

        final List<Menu> menus = menuRepository.findAllByIdIn(menuIds);
        if (menuIds.size() != menus.size()) {
            throw new NoSuchElementException("메뉴가 없으면 매장 주문을 등록할 수 없습니다.");
        }
    }

    private void validateOrderTable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new NoSuchElementException("등록되지 않은 주문 테이블에 주문을 등록할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalStateException("빈 테이블에는 매장 주문을 등록할 수 없습니다.");
        }
    }
}
