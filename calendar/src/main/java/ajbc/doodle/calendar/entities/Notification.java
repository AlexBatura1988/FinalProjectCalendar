package ajbc.doodle.calendar.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ajbc.doodle.calendar.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Notification entity class
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
@Where(clause = "disable = 0")
public class Notification {

	/**
	 * Notification id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Integer notificationId;

	/**
	 * * The owner id
	 */
	
	@Column(insertable = false, updatable = false)
	private Integer ownerId;

	/**
	 * The owner of the event
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "ownerId")
	@Where(clause = "disable = 0")
	private User owner;

	/**
	 * The event id
	 */
	
	@Column(insertable = false, updatable = false)
	private Integer eventId;

	/**
	 * The event of the notification
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
	@JoinColumn(name = "eventId")
	@Where(clause = "disable = 0")
	private Event event;

	/**
	 * The title of the notification.
	 */
	private String title = "Default Notification";

	/**
	 * Unit time to notify before the event.
	 */
	@Enumerated(EnumType.STRING)
	private Unit unit = Unit.HOURS;

	/**
	 * The amount of time to notify before the event.
	 */
	private Integer timeBeforeEvent = 24;

	/**
	 * If the notification deleted in soft delete
	 */
	@JsonIgnore
	private Integer disable = 0;

	public Notification(User owner, Event event) {
		this.owner = owner;
		this.event = event;
	}
	
	public Notification(String title, Unit unit, Integer timeBeforeEvent) {
        this.title = title;
        this.unit = unit;
        this.timeBeforeEvent = timeBeforeEvent;
    }

	public Notification(User user, Event event, String title, Unit unit, Integer timeBeforeEvent) {
		this(user, event);
		this.title = title;
		this.unit = unit;
		this.timeBeforeEvent = timeBeforeEvent;
	}
	
	/**
     * Merge the notification data into this
     * @param notification - the modified notification
     */
    public void merge(Notification notification) {
        this.title = notification.title;
        this.unit = notification.unit;
        this.timeBeforeEvent = notification.timeBeforeEvent;
    }

}
