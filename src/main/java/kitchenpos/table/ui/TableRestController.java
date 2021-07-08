package kitchenpos.table.ui;

import io.swagger.annotations.ApiOperation;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest request) {
        final OrderTableResponse created = tableService.create(request);
        return ResponseEntity.created(URI.create("/api/tables/" + created.getId()))
                             .body(created);
    }

    @ApiOperation("전체 테이블 현황 조회")
    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @ApiOperation("테이블을 빈 상태로 변경")
    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableChangeEmptyRequest request) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, request));
    }

    @ApiOperation("테이블 손님 수를 변경")
    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableChangeNumberOfGuestsRequest request) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, request));
    }
}
