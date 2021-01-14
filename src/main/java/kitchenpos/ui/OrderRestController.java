package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class OrderRestController {
	private final OrderService orderService;

	public OrderRestController(final OrderService orderService) {
		this.orderService = orderService;
	}

	//주문을 등록한다.
	// - id, 주문한테이블id, 주문상태, 주문시간,OrderLineItem
		//private Long seq;
		//private Long orderId;
		//private Long menuId;
		//private long quantity;
		//-- OrderLineItem를 조회해서 비었으면 에러
		//-- OrderLineItem를에서 MenuId 목록을 뽑아낸다.
		//-- OrderLineItem와 menuIds목록의 갯수는 같아야한다.
	//savedOrderLineItems를 생성한다.
	// 주문목록을 조회한다.
	//주문 상태를 업데이트한다.
		// -- Complete인 상태 값이 조회될 순 없다.
		// -- orderLineItem을 채운다.
	@PostMapping("/api/orders")
	public ResponseEntity<Order> create(@RequestBody final Order order) {
		final Order created = orderService.create(order);
		final URI uri = URI.create("/api/orders/" + created.getId());
		return ResponseEntity.created(uri)
				.body(created);
	}

	@GetMapping("/api/orders")
	public ResponseEntity<List<Order>> list() {
		return ResponseEntity.ok()
				.body(orderService.list());
	}

	@PutMapping("/api/orders/{orderId}/order-status")
	public ResponseEntity<Order> changeOrderStatus(@PathVariable final Long orderId, @RequestBody final Order order) {
		return ResponseEntity.ok(orderService.changeOrderStatus(orderId, order));
	}
}
