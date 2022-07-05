package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ajbc.doodle.calendar.enums.RepeatingOptions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Events")
public class Event {
	
	
	public Event(Integer ownerId, String title, Boolean isAllDay, LocalDateTime startDate,
			LocalDateTime endDate, String address, String description, RepeatingOptions repeatingOptions,
			Integer disable) {
		this.ownerId = ownerId;
		this.title = title;
		this.isAllDay = isAllDay;
		this.startDate = startDate;
		this.endDate = endDate;
		this.address = address;
		this.description = description;
		this.repeatingOptions = repeatingOptions;
		this.disable = disable;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
	private Integer ownerId;
	private String title;
	private boolean isAllDay;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String address;
	private String description;
	//private List<Integer> guests;
	//private List<Integer> notifications;
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeatingOptions;
	private Integer disable;
	
	
	

}
