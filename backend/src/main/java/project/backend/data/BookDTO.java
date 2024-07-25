package project.backend.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDTO {
    private Integer id;
    private String title;
    private String srcImg;
    private String link;
    private int like;
    private boolean likedByThisPlayer;
}
