package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroupEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class OrderTableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public OrderTableService(OrderDao orderDao, OrderTableDao orderTableDao, OrderTableRepository orderTableRepository, OrderService orderService) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTableResponse createTemp(final OrderTableRequest orderTableRequest) {
        OrderTableEntity orderTableEntity = orderTableRepository.save(orderTableRequest.toEntity());

        return OrderTableResponse.of(orderTableEntity);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> listTemp() {
        List<OrderTableEntity> orderTableEntities = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTableEntities);

    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정된 테이블은 변경할 수 없습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeEmptyTemp(Long orderTableId, OrderTableRequest orderTableRequest) {
        final OrderTableEntity savedOrderTable = findById(orderTableId);

        changeEmptyValidCheck(savedOrderTable);

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문 고객 수는 0명 이상이어야 합니다.");
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuestsTemp(Long orderTableId, OrderTableRequest orderTableRequest) {
        final OrderTableEntity savedOrderTable = findById(orderTableId);
        changeNumberOfGuestsValidCheck(orderTableRequest, savedOrderTable);
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }

    private void changeEmptyValidCheck(OrderTableEntity savedOrderTable) {
        tableGroupValidCheck(savedOrderTable.getTableGroup());
        orderStatusValidCheck(savedOrderTable.getId());
    }

    private void orderStatusValidCheck(Long orderTableId) {
        if (orderService.changeStatusValidCheck(orderTableId)) {
            throw new IllegalArgumentException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
        }
    }

    private void tableGroupValidCheck(TableGroupEntity tableGroupEntity) {
        if (Objects.nonNull(tableGroupEntity)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 변경할 수 없습니다.");
        }
    }

    public OrderTableEntity findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문 테이블입니다."));

    }


    private void changeNumberOfGuestsValidCheck(OrderTableRequest orderTableRequest, OrderTableEntity savedOrderTable) {
        numberOfGuestsValidCheck(orderTableRequest.getNumberOfGuests());
        emptyValidCheck(savedOrderTable.isEmpty());

    }

    private void emptyValidCheck(boolean empty) {
        if (empty) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }
    }

    private void numberOfGuestsValidCheck(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문 고객 수는 0명 이상이어야 합니다.");
        }
    }


    public List<OrderTableEntity> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

}
