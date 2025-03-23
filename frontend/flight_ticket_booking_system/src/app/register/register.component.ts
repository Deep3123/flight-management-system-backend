import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserAuthServiceService } from '../services/user-auth-service.service'; // Ensure correct import
import { User } from '../dtoClasses/user'; // Import the DTO class
import Swal from 'sweetalert2'; // SweetAlert for stylish pop-up

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  constructor(private userAuthService: UserAuthServiceService) {}
  user = new User();

  // This method is called when the form is submitted
  onRegisterSubmit(form: NgForm) {
    if (form.valid) {
      console.log(form.value);

      this.user.name = form.value.name;
      this.user.emailId = form.value.email;
      this.user.mobileNo = form.value.mobileNo;
      this.user.username = form.value.username;
      this.user.password = form.value.password;

      console.log(this.user);

      // Call the service to save the user data
      this.userAuthService.saveUserData(this.user).subscribe(
        (response) => {
          // Display success pop-up on successful registration
          Swal.fire({
            icon: 'success',
            title: 'Registration Successful!',
            text: response.message,
            confirmButtonText: 'OK',
          }).then(() => {
            form.reset(); // Reset the form after successful registration
          });
        },
        (error) => {
          console.log(error)
          // Display error pop-up if registration fails
          Swal.fire({
            icon: 'error',
            title: 'Registration Failed!',
            text: error.error.message,
            confirmButtonText: 'OK',
          });
        }
      );
    } else {
      Swal.fire({
        icon: 'warning',
        title: 'Form Invalid',
        text: 'Please fill in all fields correctly.',
        confirmButtonText: 'OK',
      });
    }
  }
}
