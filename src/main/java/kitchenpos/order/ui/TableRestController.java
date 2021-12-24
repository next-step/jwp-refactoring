package kitchenpos.order.ui;

import kitchenpos.common.BindingException;
import kitchenpos.order.application.TableService;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/api/tables")
@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeGuests(@PathVariable final Long orderTableId,
                                                           @RequestBody @Valid final OrderTableRequest request, BindingResult bs) {
        if (bs.hasErrors()) {
            throw new BindingException();
        }
        return ResponseEntity.ok().body(tableService.changeGuests(orderTableId, request));
    }

    @PutMapping("/{tableId}/order-status")
    public ResponseEntity<OrderTableResponse> changeOrderStatus(@PathVariable final Long tableId,
                                                           @RequestBody final OrderStatusRequest request) {
        return ResponseEntity.ok(tableService.changeOrderStatus(tableId, request));
    }
}
