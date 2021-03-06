package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;

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
//@Where(clause = "disable = 0")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NaturalId
	private Integer eventId;
	private String title;
	private boolean isAllDay;

	private LocalDateTime startDate;

	private LocalDateTime endDate;
	private String address;
	private String description;
	@Enumerated(EnumType.STRING)
	private RepeatingOptions repeatingOptions;
	@JsonIgnore
	private Integer disable = 0;

	@Column(insertable = false, updatable = false)
	private Integer ownerId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ownerId")
	@Where(clause = "disable = 0")
	private User owner;

	@JsonIgnore
	@OneToMany(mappedBy = "event", cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
//	@Where(clause = "disable = 0")
	private Set<Notification> notifications = new HashSet<>();
 
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "usersEvents", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
//	@Where(clause = "disable = 0")
	private Set<User> guests = new HashSet<>();

	public Event(String title, Boolean isAllDay, LocalDateTime startDate, LocalDateTime endDate, String address,
			String description, RepeatingOptions repeatingOptions, List<User> guests) {

		this.title = title;
		this.isAllDay = isAllDay;
		this.startDate = startDate;
		this.endDate = endDate;
		this.address = address;
		this.description = description;
		this.repeatingOptions = repeatingOptions;
		this.guests = new HashSet<>(guests);

	}

	public Event(User owner, String title, Boolean isAllDay, LocalDateTime startDate, LocalDateTime endDate,
			String address, String description, RepeatingOptions repeatingOptions, List<User> guests) {
		this(title, isAllDay, startDate, endDate, address, description, repeatingOptions, guests);
		this.owner = owner;
	}

	public Event(Integer eventId, String title, Boolean isAllDay, LocalDateTime startDate, LocalDateTime endDate,
			String address, String description, RepeatingOptions repeatingOptions, List<User> guests) {
		this(title, isAllDay, startDate, endDate, address, description, repeatingOptions, guests);
		this.eventId = eventId;
	}

	public void addGuests(List<User> guests) {
		Set<User> guestsSet = new HashSet<>(guests);
		this.setGuests(Stream.concat(this.getGuests().stream(), guests.stream()).collect(Collectors.toSet()));
	}
	
	public void addGuest(User guest) {
		this.guests.add(guest);
	}

	public void merge(Event event) {
		if (!this.title.equals(event.title)) {
			this.title = event.title;
		}
		if (this.isAllDay != event.isAllDay) {
			this.isAllDay = event.isAllDay;
		}
		if (!this.startDate.equals(event.startDate)) {
			this.startDate = event.startDate;
		}
		if (!this.endDate.equals(event.endDate)) {
			this.endDate = event.endDate;
		}
		if (!this.address.equals(event.address)) {
			this.address = event.address;
		}
		if (!this.description.equals(event.description)) {
			this.description = event.description;
		}
		if (!this.repeatingOptions.equals(event.repeatingOptions)) {
			this.repeatingOptions = event.repeatingOptions;
		}
	}
	public void removeGuests() {
        for (User user : this.getGuests()) {
            this.removeGuest(user);
        }
    }

    public void removeGuest(User user) {
        this.guests.remove(user);
        user.getEvents().remove(this);
    }
    
    /**
     * Returns non-disable notifications
     * @return
     */
    public List<Notification> getActiveNotifications() {
    	return this.getNotifications()
    			.stream()
    			.filter(notification -> notification.getDisable() == 0)
    			.toList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event e) {
            return this.eventId.equals(e.getEventId());
        }
        return false;
    }

}
