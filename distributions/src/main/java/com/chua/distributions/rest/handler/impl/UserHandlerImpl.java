package com.chua.distributions.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.ClientSettingsFormBean;
import com.chua.distributions.beans.PasswordFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SettingsFormBean;
import com.chua.distributions.beans.UserFormBean;
import com.chua.distributions.beans.UserRetrieveBean;
import com.chua.distributions.constants.SystemConstants;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.enums.VatType;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.UserHandler;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.EncryptionUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.StringGenerator;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 14, 2016
 */
@Transactional
@Component
public class UserHandlerImpl implements UserHandler {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private SystemConstants systemConstants;

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public User getUser(Long userId) {
		return userService.find(userId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey) {
		return userService.findAllWithPagingOrderByNameAndUserType(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<User> getClientObjectList(Integer pageNumber, String searchKey) {
		return userService.findAllClientsWithPagingOrderByBusinessName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public UserRetrieveBean retrieveUser(String username, String emailAddress) {
		final UserRetrieveBean partialUser;
		final User user = userService.findByUsernameOrEmail(username, emailAddress);
		
		if(user != null && user.getUserType().equals(UserType.CLIENT)) {
			partialUser = new UserRetrieveBean(user);
		} else {
			partialUser = null;
		}
		
		return partialUser;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createUser(UserFormBean userForm) {
		final ResultBean result;
		final ResultBean validateForm = validateUserForm(userForm);
		
		if(validateForm.getSuccess()) {
			final ResultBean validatePassword = validatePassword(userForm);
			if(validatePassword.getSuccess()) {
				if(userService.isExistByUsername(userForm.getUsername().trim())) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Username already exists.") + "  Please choose another username."));
				} else {
					final User user = new User();
					
					user.setPassword(EncryptionUtil.getMd5(userForm.getPassword()));
					setUser(user, userForm);
					
					result = new ResultBean();
					result.setSuccess(userService.insert(user) != null);
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created account of " + Html.text(Color.BLUE, user.getFormattedName()) + ". Thank you."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validatePassword;
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updateUser(UserFormBean userForm) {
		final ResultBean result;
		final User user = userService.find(userForm.getId());
		
		if(user != null) {
			final ResultBean validateForm = validateUserForm(userForm);
			
			if(validateForm.getSuccess()) {
				final User uzer = userService.findByUsername(userForm.getUsername());
				if(uzer != null && user.getId() != uzer.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Username already exists.") + " Please choose another username."));
				} else {
					setUser(user, userForm);
					
					result = new ResultBean();
					result.setSuccess(userService.update(user));
					if(result.getSuccess()) {
						if(UserContextHolder.getUser().getId().equals(user.getId())) {
							UserContextHolder.refreshUser(user);
						}
						result.setMessage(Html.line("Profile has been " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 1)
	public ResultBean removeUser(Long userId) {
		final ResultBean result;
		
		if(userId != UserContextHolder.getUser().getId()) {
			final User user = userService.find(userId);
			if(user != null) {
				if(user.getUserType().equals(UserType.ADMINISTRATOR)) {
					result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED") + " to delete an admin account.")
														+ Html.line("Please contact your database administrator for assistance."));
				} else {
					result = new ResultBean();
					
					result.setSuccess(userService.delete(user));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed the account of Mr./Ms. " + Html.text(Color.BLUE, user.getFirstName() + " " + user.getLastName()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("You " + Html.text(Color.RED, "CANNOT") + " delete your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changePassword(PasswordFormBean passwordForm) {
		final ResultBean result;
		final User user = userService.find(passwordForm.getId());
		
		if(user != null) {
			if(user.getPassword().equals(EncryptionUtil.getMd5(passwordForm.getOldPassword()))) {
				final ResultBean validatePassword = validatePassword(passwordForm);
				
				if(validatePassword.getSuccess()) {
					result = new ResultBean();
					user.setPassword(EncryptionUtil.getMd5(passwordForm.getPassword()));
					
					result.setSuccess(userService.update(user));
					if(result.getSuccess()) {
						UserContextHolder.refreshUser(user);
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " changed your password."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = validatePassword;
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Incorrect password."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 1)
	public ResultBean resetPassword(Long userId) {
		final ResultBean result;
		final User user = userService.find(userId);
		
		if(user != null) {
			result = new ResultBean();
			String randomPassword = StringGenerator.nextString();
			
			user.setPassword(EncryptionUtil.getMd5(randomPassword));
			result.setSuccess(userService.update(user) &&
					emailUtil.send(user.getEmailAddress(),
					null,
					getEmailOfAllAdminAndManagers(),
					"Prime Pad Reset Password",
					"Hi " + user.getFirstName() + " " + user.getLastName() + ", your Prime Pad account password has just been reset."
						+ "\nYour new credentials are : "
						+ "\n\nUsername - " + user.getUsername()
						+ "\nPasswrod - " + randomPassword
						+ "\n\nPlease login at " + systemConstants.getServerDomain() + " and change your password as soon as possible.",
					null));
			
			if(result.getSuccess()) {
				result.setMessage(Html.line(Color.GREEN, "Password successfully reset.") 
						+ Html.line("New password sent to " + Html.text(Color.BLUE, user.getEmailAddress()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changeSettings(SettingsFormBean settingsForm) {
		final ResultBean result;
		final User user = userService.find(settingsForm.getId());
		
		if(user != null) {
			result = new ResultBean();
			setSettings(user, settingsForm);
			
			result.setSuccess(userService.update(user));
			if(result.getSuccess()) {
				UserContextHolder.refreshUser(user);
				result.setMessage(Html.line(Color.GREEN, "Settings successfully updated."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean changeClientSettings(ClientSettingsFormBean clientSettingsForm) {
		final ResultBean result;
		final User user = userService.find(clientSettingsForm.getId());
		
		if(user != null) {
			result = new ResultBean();
			setClientSettings(user, clientSettingsForm);
			
			result.setSuccess(userService.update(user));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Color.GREEN, "Client settings successfully updated."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load user. Please re-log your account."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public List<User> getClientList() {
		return userService.findAllClientsOrderByBusinessName();
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public List<UserType> getUserTypeList() {
		return Stream.of(UserType.values())
					.collect(Collectors.toList());
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public List<Area> getAreaList() {
		return Stream.of(Area.values())
					.collect(Collectors.toList());
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public List<VatType> getVatTypeList() {
		return Stream.of(VatType.values())
					.collect(Collectors.toList());
	}
	
	@Override
	public String getEmailOfAllAdminAndManagers() {
		String emailOfAllAdminAndManagers = "";
		
		final List<User> administrators = userService.findAllAdministrators();
		final List<User> managers = userService.findAllManagers();
		
		for(User admin : administrators) {
			emailOfAllAdminAndManagers += admin.getEmailAddress() + ", ";
		}
		
		for(User manager : managers) {
			emailOfAllAdminAndManagers += manager.getEmailAddress() + ", ";
		}
		
		return emailOfAllAdminAndManagers;
	}
	
	private void setUser(User user, UserFormBean userForm) {
		user.setUsername(userForm.getUsername().trim());
		user.setFirstName(userForm.getFirstName().trim());
		user.setLastName(userForm.getLastName().trim());
		user.setEmailAddress(userForm.getEmailAddress().trim());
		user.setContactNumber(userForm.getContactNumber().trim());
		user.setUserType(userForm.getUserType() != null ? userForm.getUserType() : UserType.CLIENT);
		if(user.getItemsPerPage() == null) user.setItemsPerPage(10);
		
		if(user.getUserType().equals(UserType.CLIENT)) {
			user.setBusinessName(userForm.getBusinessName() != null ? userForm.getBusinessName().trim() : null);
			user.setBusinessAddress(userForm.getBusinessAddress() != null ? userForm.getBusinessAddress().trim() : null);
			user.setBusinessContactNumber(userForm.getBusinessContactNumber() != null ? userForm.getBusinessContactNumber().trim() : null);
			user.setBusinessArea(userForm.getBusinessArea());
			if(userForm.getBusinessArea() != null && user.getVatType() == null) user.setVatType(userForm.getBusinessArea().getVatType());
		}
	}
	
	private void setSettings(User user, SettingsFormBean settingsForm) {
		user.setItemsPerPage(settingsForm.getItemsPerPage() != null ? settingsForm.getItemsPerPage() : 10);
	}
	
	private void setClientSettings(User user, ClientSettingsFormBean clientSettingsForm) {
		user.setVatType(clientSettingsForm.getVatType() != null ? clientSettingsForm.getVatType() : VatType.VAT);
	}
	
	private ResultBean validateUserForm(UserFormBean userForm) {
		final ResultBean result;
		
		if(userForm.getUsername() == null || userForm.getUsername().trim().length() < 3 ||
				userForm.getFirstName() == null || userForm.getFirstName().trim().length() < 3 ||
				userForm.getLastName() == null || userForm.getLastName().trim().length() < 3 ||
				userForm.getEmailAddress() == null || userForm.getEmailAddress().trim().length() < 3 ||
				userForm.getContactNumber() == null || userForm.getContactNumber().trim().length() < 3 ||
				((userForm.getUserType() == null || userForm.getUserType().equals(UserType.CLIENT)) &&
				(userForm.getBusinessName() == null || userForm.getBusinessName().trim().length() < 3 ||
				userForm.getBusinessAddress() == null || userForm.getBusinessAddress().trim().length() < 3 ||
				userForm.getBusinessContactNumber() == null || userForm.getBusinessContactNumber().trim().length() < 3 ||
				userForm.getBusinessArea() == null))) {
			
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters."));
		} else if(!userForm.getUsername().trim().matches("^[A-Za-z_]\\w{2,31}$")) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Invalid Username!")
												+ Html.line("Username must be at least 3 to 30 characters and cannot contain white spaces and/or special characters."));
		} else if(!emailUtil.validateEmail(userForm.getEmailAddress().trim())) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Invalid Email Address!"));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
	
	private ResultBean validatePassword(UserFormBean userForm) {
		return validatePassword(new PasswordFormBean("", userForm.getPassword(), userForm.getConfirmPassword()));
	}
	
	private ResultBean validatePassword(PasswordFormBean passwordForm) {
		final ResultBean result;
		
		if(passwordForm.getPassword() == null || passwordForm.getPassword().length() < 3 ||
				passwordForm.getConfirmPassword() == null || passwordForm.getConfirmPassword().length() < 3) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters."));
		} else if(!passwordForm.getPassword().equals(passwordForm.getConfirmPassword())) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Confirm password does not match."));
		} else if(!passwordForm.getPassword().matches("((?=.*\\d)(?=.*[a-zA-Z])\\S{5,21})")) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Invalid Password!")
												+ Html.line("Password must be at least 6 to 20 characters and cannot contain white spaces and must be a combination of letters and digits."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
