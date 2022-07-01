package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        // 주문에 주문 항목이 없는 경우 예외 처리
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        // 존재하지 않는 메뉴를 주문한 경우 예외 처리
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        // 주문에 포함된 주문 테이블이 DB에 존재하지 않는 경우 예외 처리
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // TODO 요청 객체의 정적 팩토리 메소드를 이용하여 초기값을 가지는 Order Entity의 생성을 유도
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        // TODO : Order Entity와 OrderLineItem Entity의 생명 주기가 같도록 영속성 전이 옵션 설정
        // TODO : e.g. @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        final Long orderId = savedOrder.getId();
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(orderId);
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    // TODO N+1 발생 : N개의 주문을 조회하는 경우 이므로 1:1 관계는 Fetch Join으로, 1:N 관계는 LazyLoading + Batch Size를 이용한 In절로 접근
    // JPA로 리팩토링을 진행하면서 orderLineItem이 다른 Entity와 연관관계를 가지는 경우 또 다른 N+1이 발생할 수 있음에 주의
    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        // TODO : 주문 상태 변경 로직을 주문 도메인 안쪽으로 이동 후, 해당 로직에서 주문 상태 변경 가능에 대한 유효성 검증 처리
        // TODO : 문제 추적 및 파악이 용이하도록 예외 처리 시 오류 문구를 포함
        // TODO : 주문 객체 생성 시, 초기 주문 상태를 할당하지 않은 경우 NPE 발생
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        // TODO : 주문 상태 변경 로직을 주문 도메인 안쪽으로 이동 하여 setter가 아닌 주문 도메인이 직접 필드값 변경
        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        // TODO : 트랜잭션 내에서 필드 값 변경을 감지하여 갱신 쿼리 발생하도록 유도
        orderDao.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
