package com.example.b07project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.widget.Toast;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

   @Mock
   SignInActivityView view;

   @Mock
   SignInActivityModel model;



   @Test
   public void testEmptyUsername() {
      String username = "";
      String password = "xyz";
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      doAnswer(invocation -> {
         view.showToast("Username does not exist. Please try again.");
         return null;
      }).when(model).queryUsernameAndPassword(username, password, presenter);
      when(view.getPassword()).thenReturn(password);
      when(view.getUsername()).thenReturn(username);
      presenter.signInClicked();
      verify(view).showToast(ArgumentMatchers.eq("Username does not exist. Please try again."));

   }

   @Test
   public void testEmptyPassword(){
      String username = "user1";
      String actualPassword = "123";
      String attemptedPassword = "";
      String hashedPassword = BCrypt.withDefaults().hashToString(12, actualPassword.toCharArray());
      boolean isAdmin = false;
      User user = new User(hashedPassword, isAdmin);
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      presenter.checkUsernameAndPassword(username, attemptedPassword, user);
      verify(view).showToast(ArgumentMatchers.eq("Incorrect password. Please try again."));
   }

   @Test
   public void  testEmptyUserAndPassword(){
      String username = "user1";
      String actualPassword = "123";
      String attemptedPassword = "";
      String hashedPassword = BCrypt.withDefaults().hashToString(12, actualPassword.toCharArray());
      boolean isAdmin = false;
      User user = new User(hashedPassword, isAdmin);
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      presenter.checkUsernameAndPassword(username, attemptedPassword, user);
      verify(view).showToast(ArgumentMatchers.eq("Incorrect password. Please try again."));
   }


   @Test
   public void testValidUsernameInvalidPassword(){
      String username = "user1";
      String actualPassword = "123";
      String attemptedPassword = "456";
      String hashedPassword = BCrypt.withDefaults().hashToString(12, actualPassword.toCharArray());
      boolean isAdmin = false;
      User user = new User(hashedPassword, isAdmin);
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      presenter.checkUsernameAndPassword(username, attemptedPassword, user);
      verify(view).showToast(ArgumentMatchers.eq("Incorrect password. Please try again."));

   }

   @Test
   public void testInvalidUsername(){
      String username = "user";
      String password = "xyz";
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      doAnswer(invocation -> {
         view.showToast("Username does not exist. Please try again.");
         return null;
      }).when(model).queryUsernameAndPassword(username, password, presenter);
      when(view.getPassword()).thenReturn(password);
      when(view.getUsername()).thenReturn(username);
      presenter.signInClicked();
      verify(view).showToast(ArgumentMatchers.eq("Username does not exist. Please try again."));

   }

   @Test
   public void testValidLoginSuccess(){
      String username = "user1";
      String actualPassword = "123";
      String attemptedPassword = "123";
      String hashedPassword = BCrypt.withDefaults().hashToString(12, actualPassword.toCharArray());
      boolean isAdmin = false;
      User user = new User(hashedPassword, isAdmin);
      SignInActivityPresenter presenter = new SignInActivityPresenter(view, model);
      presenter.checkUsernameAndPassword(username, attemptedPassword, user);
      verify(view).showToast(ArgumentMatchers.eq("Welcome back " + username + "!"));

   }


}
