package ajbc.doodle.calendar.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
