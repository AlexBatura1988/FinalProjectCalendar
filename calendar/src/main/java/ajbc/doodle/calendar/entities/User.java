package ajbc.doodle.calendar.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private Integer emailId;
	private String firstName;
	private String lastName;
	private String email;
	private LocalDate birthDate;
	private LocalDate joinDate;
	private Integer disable;
	
	@JsonIgnore
	@ManyToMany(mappedBy="guests",cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
	//List<Event> events;
	private Set<Event> events = new HashSet<>();
	
	

}
