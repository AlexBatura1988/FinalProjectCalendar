package ajbc.doodle.calendar.entities;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Table(name = "notifications")
public class Notification {

//	private int id;
//	private LocalDateTime localDateTime;
//	private String title;
//	private String message;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	private String title;
	private TimeUnit unit;
	private Integer period;

}
