package kitchenpos.table.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @ApiOperation("주문 받을 테이블 생성")
    @PostMapping("/api/tables")
    public ResponseEntity<OrderTable> create(@RequestBody final OrderTable orderTable) {
        final OrderTable created = tableService.create(orderTable);
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @ApiOperation("전체 테이블 현황 조회")
    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTable>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @ApiOperation("테이블을 빈 상태로 변경")
    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTable> changeEmpty(@PathVariable final Long orderTableId,
                                                  @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @ApiOperation("테이블 손님 수를 변경")
    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTable> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                           @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
