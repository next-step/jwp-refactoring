package kitchenpos.order.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableDao orderTableDao
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        // 저장해야될 주문항목이 비어있으면 안된다.
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        // 주문항목의 메뉴 리스트 아이디를 가져온다.
        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(toList());

        // 주문항목과 메뉴의 개수는 같아야한다.
        if (orderRequest.getOrderLineItems().size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(orderLineItem -> {
                    Menu menu = menuRepository.findById(orderLineItem.getMenuId()).orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu, orderLineItem.getQuantity());
                }).collect(toList());


        OrderTable orderTable1 = orderTableDao.findById(orderRequest.getOrderTableId()).orElseThrow(EntityNotFoundException::new);
        final Order savedOrder = orderRepository.save(new Order(orderTable1));
        savedOrder.addOrderLineItems(orderLineItems);
        orderLineItems.forEach(orderLineItemRepository::save);
        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }
}
