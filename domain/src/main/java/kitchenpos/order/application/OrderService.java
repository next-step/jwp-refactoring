package kitchenpos.order.application;

import java.util.List;
import kitchenpos.exception.NotCreateOrderException;
import kitchenpos.exception.NotFoundEntityException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.order.dto.CreateOrderDto;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static java.util.stream.Collectors.toList;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
                        OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(CreateOrderDto createOrderDto) {

        if (CollectionUtils.isEmpty(createOrderDto.getOrderLineItems())) {
            throw new NotCreateOrderException("주문 항목을 1개 이상 입력해야 합니다.");
        }

        final List<OrderLineItem> orderLineItems =
            createOrderDto.getOrderLineItems()
                    .stream()
                    .map(dto -> {
                        Menu menu = menuRepository.findById(dto.getMenuId())
                                                  .orElseThrow(NotFoundEntityException::new);
                        return new OrderLineItem(menu, dto.getQuantity());
                    })
                    .collect(toList());

        final List<Long> menuIds = orderLineItems.stream()
                                                 .map(orderLineItem -> orderLineItem.getMenu().getId())
                                                 .distinct()
                                                 .collect(toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotCreateOrderException("입력한 메뉴에 중복이 있습니다.");
        }

        OrderTable orderTable = orderTableRepository.findById(createOrderDto.getOrderTableId())
                                                          .orElseThrow(NotFoundEntityException::new);

        Order order = new Order(orderLineItems);
        orderTable.addOrder(order);

        Order savedOrder = orderRepository.save(order);
        orderLineItems.forEach(orderLineItemRepository::save);

        return savedOrder;
    }

    public List<OrderDto> list() {
        return orderRepository.findAll()
                              .stream()
                              .map(OrderDto::of)
                              .collect(toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(Long orderId, ChangeOrderStatusDto changeOrderStatusDto) {
        Order savedOrder = orderRepository.findById(orderId)
                                          .orElseThrow(NotFoundEntityException::new);

        savedOrder.changeOrderStatus(changeOrderStatusDto.getOrderStatus());
        return OrderDto.of(savedOrder);
    }
}
