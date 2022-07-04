package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.order.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.CreateTableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest createTableGroupRequest) {
        final List<Long> orderTableIds = createTableGroupRequest.getOrderTables();

        // 그룹핑할 주문 테이블 정보가 없거나, 한개인 경우 예외 처리
        // TODO 테이블 그룹 Entity 생성 시점에 유효성을 검증하여 그룹핑 대상을 보장받을 수 있도록 수정
        // TODO 최소 테이블 그룹 조건인 2를 상수로 추출, e.g. MINIMUM_GROUPING_TARGET_SIZE = 2
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        // DB에 존재하지 않는 주문 테이블이 포함된 경우 예외 처리
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        // 비어있는 주문 테이블이 포함되거나, 이미 그룹핑 된 주문 테이블이 포함된 경우 예외 처리
        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
        TableGroup tableGroup = createTableGroupRequest.toTableGroup(savedOrderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());

        /**
         * 테이블 그룹 Entity가 생성됨에 따라 주문 테이블의 tableGroupId 필드 값의 생명 주기를
         * 영속성 전이를 통해 같이 관리 할 수 있는지 접근 시도
         */
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.allocateTableGroup(savedTableGroup);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        System.out.println(orderTables.size());

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        // TODO 오류 파악 및 디버깅이 용이하도록 예외 처리 시 오류 문구 설정
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        // TODO : 일급 컬렉션 + 변경 감지 + 영속성 전이
        for (final OrderTable orderTable : orderTables) {
            orderTable.deallocateTableGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
