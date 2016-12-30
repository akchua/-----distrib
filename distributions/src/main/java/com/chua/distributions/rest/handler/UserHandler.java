package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.ClientSettingsFormBean;
import com.chua.distributions.beans.PartialUserBean;
import com.chua.distributions.beans.PasswordFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SettingsFormBean;
import com.chua.distributions.beans.UserFormBean;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.enums.VatType;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 14, 2016
 */
public interface UserHandler {
	
	User getUser(Long userId);
	
	ObjectList<User> getUserObjectList(Integer pageNumber, String searchKey);
	
	ObjectList<User> getClientObjectList(Integer pageNumber, String searchKey);
	
	PartialUserBean retrieveUser(String username, String emailAddress);
	
	ResultBean createUser(UserFormBean userForm);
	
	ResultBean updateUser(UserFormBean userForm);
	
	ResultBean removeUser(Long userId);
	
	ResultBean changePassword(PasswordFormBean passwordForm);
	
	ResultBean resetPassword(Long userId);
	
	ResultBean changeSettings(SettingsFormBean settingsForm);
	
	ResultBean changeClientSettings(ClientSettingsFormBean clientSettingsForm);
	
	List<UserType> getUserTypeList();
	
	List<Area> getAreaList();
	
	List<VatType> getVatTypeList();
}
