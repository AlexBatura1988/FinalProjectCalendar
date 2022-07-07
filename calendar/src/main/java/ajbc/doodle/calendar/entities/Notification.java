package ajbc.doodle.calendar.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ajbc.doodle.calendar.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
public class Notification {


	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Integer notificationId;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="userId")
	private User user;
	
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name="eventId")
	private Event event;
	
	
	private String title;
	@Enumerated(EnumType.STRING)
	private Unit unit;
	private Integer timeBeforeEvent;
	private Integer disable;
	
	
	
	
	
	
	public Notification( User user,Event event,String title, Unit unit, Integer timeBeforeEvent,Integer disable) {
		this.user= user;
		this.event = event;
		this.title = title;
		this.unit = unit;
		this.timeBeforeEvent = timeBeforeEvent;
		this.disable = disable;
				
	}
	
	

}
