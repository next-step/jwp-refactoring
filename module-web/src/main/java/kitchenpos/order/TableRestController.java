package kitchenpos.order;

import kitchenpos.exception.BindingException;
import kitchenpos.order.application.TableService;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
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
    public ResponseEntity<TableResponse> create(@RequestBody @Valid final TableRequest request, BindingResult bs) {
        if (bs.hasErrors()) {
            throw new BindingException();
        }
        final TableResponse created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<TableResponse> changeEmpty(@PathVariable final Long orderTableId) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<TableResponse> changeGuests(@PathVariable final Long orderTableId,
                                                      @RequestBody @Valid final TableRequest request, BindingResult bs) {
        if (bs.hasErrors()) {
            throw new BindingException();
        }
        return ResponseEntity.ok().body(tableService.changeGuests(orderTableId, request));
    }

    @PutMapping("/{tableId}/order-status")
    public ResponseEntity<TableResponse> changeOrderStatus(@PathVariable final Long tableId,
                                                           @RequestBody final OrderStatusRequest request) {
        return ResponseEntity.ok(tableService.changeOrderStatus(tableId, request));
    }
}
