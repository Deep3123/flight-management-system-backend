import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginReq } from '../dtoClasses/login-req';
import Swal from 'sweetalert2';
import { UserAuthServiceService } from '../services/user-auth-service.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  constructor(private userAuthService: UserAuthServiceService) { }

  login = new LoginReq();
  onSubmit(form: any) {
    if (form.valid) {
      console.log(form.value);

      this.login.username = form.value.username
      this.login.password = form.value.password

      console.log(this.login);

      // Call the service to save the user data
      this.userAuthService.userLogin(this.login).subscribe(
        (response) => {
          // Display success pop-up on successful registration
          Swal.fire({
            icon: 'success',
            title: 'Login Successful!',
            text: 'You have successfully logged in.',
            confirmButtonText: 'OK',
          }).then(() => {
            form.reset(); // Reset the form after successful registration
          });
          // console.log(response.token)
          localStorage.setItem('token', response.token)
        },
        (error) => {
          // Display error pop-up if registration fails
          Swal.fire({
            icon: 'error',
            title: 'Login Failed!',
            text: error.error._message,
            confirmButtonText: 'OK',
          });
          console.log(error);
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
