package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
//@Where(clause = "disable = 0")
public class User {

	public User(String firstName, String lastName, String email, LocalDate birthDate, LocalDate joinDate,
			Integer disable) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
		this.disable = disable;
	}
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NaturalId
	private Integer userId;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String email;
	private LocalDate birthDate;
	private LocalDate joinDate;
	@JsonIgnore
	private Integer disable;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String endPoint;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String p256dh;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String auth;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isSubscribed;
	
	

	@JsonIgnore
	@ManyToMany(mappedBy = "guests", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//	@Where(clause = "disable = 0")
	private Set<Event> events = new HashSet<>();
	 
	public Set<Event> removeAllEvents() {
		 Set<Event> events = this.getEvents();
        for(Event event :events) {
            this.removeEvent(event);
        }
        return events;
    }

    public void removeEvent(Event event) {
        this.getEvents().remove(event);
        event.getGuests().remove(this);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User u) {
            return this.userId.equals(u.getUserId());
        }
        return false;
    }

}
