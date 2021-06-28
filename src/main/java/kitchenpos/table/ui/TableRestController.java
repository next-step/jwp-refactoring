package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableDto> create(@RequestBody OrderTableDto orderTableDto) {
        final OrderTable created = tableService.create(orderTableDto);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(OrderTableDto.of(created));
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableDto>> list() {
        return ResponseEntity.ok()
                             .body(tableService.list()
                                               .stream()
                                               .map(OrderTableDto::of)
                                               .collect(toList()));
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableDto> changeEmpty(@PathVariable Long orderTableId,
                                                     @RequestBody Boolean empty) {
        return ResponseEntity.ok().body(OrderTableDto.of(tableService.changeEmpty(orderTableId, empty)));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                              @RequestBody Integer numberOfGuests) {
        return ResponseEntity.ok().body(OrderTableDto.of(tableService.changeNumberOfGuests(orderTableId, numberOfGuests)));
    }
}
