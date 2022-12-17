package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreateValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderCreateValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        checkValidNullAndEmptyTable(order.getOrderTableId());
        validMenuCount(order.menuIds());
    }

    private void checkValidNullAndEmptyTable(Long orderTableId) {
        checkNullTableId(orderTableId);
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다"));
        orderTable.checkEmptyTable();
    }

    private void checkNullTableId(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("요청 주문 테이블 id는 null이 아니어야 합니다");
        }
    }

    private void validMenuCount(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("요청한 메뉴 갯수와 저장된 메뉴 갯수가 일치하지 않습니다");
        }
    }
}
