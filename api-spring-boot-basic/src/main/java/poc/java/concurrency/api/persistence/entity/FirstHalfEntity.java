package poc.java.concurrency.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mytable")
public class FirstHalfEntity {
  @Id @Column(name = "id") private Long id;
  @Column(name = "pro") private String pro;
  @Column(name = "firsthalf") private String firstHalf;
}
