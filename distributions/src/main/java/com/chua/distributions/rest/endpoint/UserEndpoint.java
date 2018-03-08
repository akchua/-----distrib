package com.chua.distributions.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.ClientSettingsFormBean;
import com.chua.distributions.beans.PasswordFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SettingsFormBean;
import com.chua.distributions.beans.UserFormBean;
import com.chua.distributions.beans.UserRetrieveBean;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.enums.VatType;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.UserHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 14, 2016
 */
@Path("/user")
public class UserEndpoint {

	@Autowired
	private UserHandler userHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public User getUser(@QueryParam("userId") Long userId) {
		return userHandler.getUser(userId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<User> getUserObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return userHandler.getUserObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/clientlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<User> getClientObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("area") Area area) {
		return userHandler.getClientObjectList(pageNumber, searchKey, area);
	}
	
	@GET
	@Path("/userretrieval")
	@Produces({ MediaType.APPLICATION_JSON })
	public UserRetrieveBean retrieveUser(@QueryParam("retrievalKey") String retrievalKey) {
		return userHandler.retrieveUser(retrievalKey, retrievalKey);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveUser(@FormParam("userFormData") String userFormData) throws IOException {
		final ResultBean result;

		final UserFormBean userForm = new ObjectMapper().readValue(userFormData, UserFormBean.class);
		if(userForm.getId() != null) {
			result = userHandler.updateUser(userForm);
		} else {
			result = userHandler.createUser(userForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeUser(@FormParam("userId") Long userId) {
		return userHandler.removeUser(userId);
	}
	
	@POST
	@Path("/changepassword")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePassword(@FormParam("passwordFormData") String passwordFormData) throws IOException {
		final PasswordFormBean passwordForm = new ObjectMapper().readValue(passwordFormData, PasswordFormBean.class);
		return userHandler.changePassword(passwordForm);
	}
	
	@POST
	@Path("/resetpassword")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean resetPassword(@FormParam("userId") Long userId) {
		return userHandler.resetPassword(userId);
	}
	
	@POST
	@Path("/changesettings")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changeSettings(@FormParam("settingsFormData") String settingsFormData) throws IOException {
		final SettingsFormBean settingsForm = new ObjectMapper().readValue(settingsFormData, SettingsFormBean.class);
		return userHandler.changeSettings(settingsForm);
	}
	
	@POST
	@Path("/changeclientsettings")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changeClientSettings(@FormParam("clientSettingsFormData") String clientSettingsFormData) throws IOException {
		final ClientSettingsFormBean clientSettingsForm = new ObjectMapper().readValue(clientSettingsFormData, ClientSettingsFormBean.class);
		return userHandler.changeClientSettings(clientSettingsForm);
	}
	
	@GET
	@Path("/fullclientlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<User> getFullClientList() {
		return userHandler.getClientList();
	}
	
	@GET
	@Path("/usertype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<UserType> getUserTypeList() {
		return userHandler.getUserTypeList();
	}
	
	@GET
	@Path("/area")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Area> getAreaList() {
		return userHandler.getAreaList();
	}
	
	@GET
	@Path("/vattype")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<VatType> getVatTypeList() {
		return userHandler.getVatTypeList();
	}
}
