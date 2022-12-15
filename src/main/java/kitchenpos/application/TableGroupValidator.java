package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTables;

@Component
public class TableGroupValidator {
    private static final int MIN_ORDER_TABLE_SIZE = 2;

    public void validateCreate(List<Long> orderTableIds, OrderTables savedOrderTables) {
        if (orderTableIds.isEmpty()) {
            throw new IllegalArgumentException("최소 2개 이상의 주문테이블 ID를 입력하셔야 합니다.");
        }

        if (orderTableIds.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("최소 2개 이상의 주문테이블 ID를 입력하셔야 합니다.");
        }

        if (!savedOrderTables.hasSize(orderTableIds.size())) {
            throw new IllegalArgumentException("등록되어있지 않는 주문 테이블 ID를 입력하셨습니다.");
        }
        if (savedOrderTables.hasAnyNotEmpty()) {
            throw new IllegalArgumentException("모든 주문 테이블은 빈 테이블 상태이어야 합니다.");
        }

        if (savedOrderTables.hasAnyTableGroupRegistered()) {
            throw new IllegalArgumentException("이미 단체 지정되어있는 테이블이 존재합니다.");
        }
    }
}
