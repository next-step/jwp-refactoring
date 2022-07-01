package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        // 그룹핑할 주문 테이블 정보가 없거나, 한개인 경우 예외 처리
        // TODO 테이블 그룹 Entity 생성 시점에 유효성을 검증하여 그룹핑 대상을 보장받을 수 있도록 수정
        // TODO 최소 테이블 그룹 조건인 2를 상수로 추출, e.g. MINIMUM_GROUPING_TARGET_SIZE = 2
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        // DB에 존재하지 않는 주문 테이블이 포함된 경우 예외 처리
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        // 비어있는 주문 테이블이 포함되거나, 이미 그룹핑 된 주문 테이블이 포함된 경우 예외 처리
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        /**
         * 테이블 그룹 Entity가 생성됨에 따라 주문 테이블의 tableGroupId 필드 값의 생명 주기를
         * 영속성 전이를 통해 같이 관리 할 수 있는지 접근 시도
         */
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        System.out.println(orderTables.size());

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableDao.save(orderTable);
        }
    }
}
