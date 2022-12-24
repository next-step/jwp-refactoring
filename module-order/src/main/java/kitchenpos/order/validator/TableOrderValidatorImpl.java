package kitchenpos.order.validator;//package kitchenpos.order.validator;
//
//import kitchenpos.order.domain.Order;
//import kitchenpos.order.repository.OrderRepository;
//import kitchenpos.table.validator.TableOrderValidator;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.EntityNotFoundException;
//
//@Component
//public class TableOrderValidatorImpl implements TableOrderValidator {
//
//    private final OrderRepository orderRepository;
//
//    public TableOrderValidatorImpl(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    @Override
//    public boolean isComplete(Long orderTableId) {
//        Order order = orderRepository.findByOrderTableId(orderTableId).orElseThrow(EntityNotFoundException::new);
//        return order.isComplete();
//    }
//}
