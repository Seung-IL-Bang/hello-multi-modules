package memory.library;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Memory {

    public long used;
    public long max;

    public Memory(long used, long max) {
        this.used = used;
        this.max = max;
    }
}
