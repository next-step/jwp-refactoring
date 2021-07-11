package kitchenpos.table.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.CreateOrderTableDto;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.table.dto.UpdateEmptyDto;
import kitchenpos.table.dto.UpdateNumberOfGuestsDto;
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
    public ResponseEntity<OrderTableDto> create(@RequestBody CreateOrderTableDto createOrderTableDto) {
        final OrderTable created = tableService.create(createOrderTableDto);
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
    public ResponseEntity<OrderTableDto> changeEmpty2(@PathVariable Long orderTableId,
                                                      @RequestBody UpdateEmptyDto updateEmptyDto) {
        return ResponseEntity.ok().body(OrderTableDto.of(tableService.changeEmpty(orderTableId, updateEmptyDto.isEmpty())));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableDto> changeNumberOfGuests(@PathVariable Long orderTableId,
                                                              @RequestBody UpdateNumberOfGuestsDto updateNumberOfGuestsDto) {
        return ResponseEntity.ok().body(OrderTableDto.of(
            tableService.changeNumberOfGuests(orderTableId, updateNumberOfGuestsDto.getNumberOfGuests())));
    }
}
