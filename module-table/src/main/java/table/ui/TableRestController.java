package table.ui;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import table.application.TableService;
import table.dto.OrderTableRequestDto;
import table.dto.OrderTableResponseDto;

@RestController
public class TableRestController {

    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponseDto> create(@RequestBody final OrderTableRequestDto request) {
        OrderTableResponseDto created = tableService.create(request);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponseDto>> list() {
        return ResponseEntity.ok()
                .body(tableService.list())
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponseDto> changeEmpty(
            @PathVariable final Long orderTableId
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeEmpty(orderTableId))
                ;
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponseDto> changeNumberOfGuests(
            @PathVariable final Long orderTableId,
            @RequestBody final OrderTableRequestDto orderTableRequestDto
    ) {
        return ResponseEntity.ok()
                .body(tableService.changeNumberOfGuests(orderTableId, orderTableRequestDto.getNumberOfGuests()))
                ;
    }
}
