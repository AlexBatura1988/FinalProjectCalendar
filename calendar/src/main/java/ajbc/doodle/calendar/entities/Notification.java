package ajbc.doodle.calendar.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ajbc.doodle.calendar.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
public class Notification {


	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;
	private Integer eventId;
	private String title;
	@Enumerated(EnumType.STRING)
	private Unit unit;
	private Integer timeBeforeEvent;
	private Integer disable;
	
	
	public Notification(Integer eventId, String title, Unit unit, Integer timeBeforeEvent,Integer disable) {
		super();
		this.eventId = eventId;
		this.title = title;
		this.unit = unit;
		this.timeBeforeEvent = timeBeforeEvent;
		this.disable = disable;
				
	}
	
	

}
