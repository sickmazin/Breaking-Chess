package project.backend.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@OptimisticLocking(type= OptimisticLockType.VERSION)
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String srcImg;
    private String link;
    @NotNull
    @Column(name = "\"like\"", nullable = false)
    private int like;
    @Version
    private Long version;

}
