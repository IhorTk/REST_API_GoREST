package PODJO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;

}
