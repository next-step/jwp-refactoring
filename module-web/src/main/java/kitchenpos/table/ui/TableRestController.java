package kitchenpos.table.ui;

import java.net.*;
import java.util.*;

import kitchenpos.common.NotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.table.application.*;
import kitchenpos.table.dto.*;

@RestController
@RequestMapping("/api/tables")
public class TableRestController {
    private final static String ORDER_TABLE = "주문 테이블";

    private final TableService tableService;
    private final OrderTableRepository orderTableRepository;

    public TableRestController(final TableService tableService, final OrderTableRepository orderTableRepository) {
        this.tableService = tableService;
        this.orderTableRepository = orderTableRepository;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> saveOrderTable(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse created = tableService.saveOrderTable(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> findAllOrderTable() {
        return ResponseEntity.ok()
                .body(tableService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTableResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
            .body(OrderTableResponse.of(orderTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ORDER_TABLE)))
            );
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(
            @PathVariable final Long orderTableId, @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
