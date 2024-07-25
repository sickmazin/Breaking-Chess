package project.backend.data;

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
