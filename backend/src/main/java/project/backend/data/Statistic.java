package project.backend.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Statistic {
    private String modality;
    private int lose;
    private int draw;
    private int win;
}
