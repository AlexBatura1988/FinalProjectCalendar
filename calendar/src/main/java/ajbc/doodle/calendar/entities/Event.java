package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
			Integer disable,List<User> guests) {
		this.ownerId = ownerId;
		this.title = title;
		this.isAllDay = isAllDay;
		this.startDate = startDate;
		this.endDate = endDate;
		this.address = address;
		this.description = description;
		this.repeatingOptions = repeatingOptions;
		this.disable = disable;
		this.guests = guests;
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
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeatingOptions;
	private Integer disable;
	
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "usersEvents", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> guests;
	
	
	

}
