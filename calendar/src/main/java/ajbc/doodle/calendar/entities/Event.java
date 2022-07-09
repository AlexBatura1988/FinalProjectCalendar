package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

	public Event(String title, Boolean isAllDay, LocalDateTime startDate, LocalDateTime endDate, String address,
			String description, RepeatingOptions repeatingOptions, List<User> guests) {

		this.title = title;
		this.isAllDay = isAllDay;
		this.startDate = startDate;
		this.endDate = endDate;
		this.address = address;
		this.description = description;
		this.repeatingOptions = repeatingOptions;
		this.guests = guests;

	}

	public Event(User owner, String title, Boolean isAllDay, LocalDateTime startDate, LocalDateTime endDate,
			String address, String description, RepeatingOptions repeatingOptions, List<User> guests) {
		this(title, isAllDay, startDate, endDate, address, description, repeatingOptions, guests);
		this.owner = owner;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eventId;
    private String title;
    private boolean isAllDay;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
    private String address;
    private String description;
    @Enumerated(EnumType.STRING)
    private RepeatingOptions repeatingOptions;
    private Integer disable = 0;

    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Integer ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User owner;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "usersEvents", joinColumns = @JoinColumn(name = "eventId"), inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<User> guests;

}
