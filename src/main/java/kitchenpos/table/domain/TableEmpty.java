package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TableEmpty {

    @Column(nullable = false)
    private boolean empty;

    protected TableEmpty() {
    }

    public TableEmpty(Boolean empty){
        validate(empty);
        this.empty = empty;
    }

    private void validate(Boolean empty){
        if(empty == null){
            throw new IllegalArgumentException("[ERROR] 테이블 상태는 null 일 수 없습니다.");
        }
    }

    public boolean isEmptyTable(){
        return empty;
    }

    public boolean isOrderTable(){
        return !empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
