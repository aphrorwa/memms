/**
 * Copyright (c) 2012, Clinton Health Access Initiative.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chai.memms.security

import org.chai.location.CalculationLocation;
import org.chai.location.DataLocation;
import org.chai.location.Location;
import org.chai.location.LocationLevel;
import org.chai.memms.maintenance.Notification;
import org.chai.memms.util.Utils

class User {
	def locationService
	enum UserType{
		
		ADMIN("admin"),
		SYSTEM("system"),
		DATACLERK("dataclerk"),
		TECHNICIANFACILITY("technician.facility"),
		TECHNICIANMOH("technician.moh"),
		OTHER("other");
		
		String messageCode = "user.type";
		final String name
		UserType(String name){ this.name=name; }
		String getKey() { return name() }
	}
	
    static String PERMISSION_DELIMITER = ";"
	
	//Needed to enable cascading deletes, these fields should not be collections since
	//some code has been written assuming its a one to many relationship
	RegistrationToken registrationToken
	PasswordToken passwordToken
	
	
	String email
    String username
	String uuid
    String passwordHash = ''
	String permissionString = ''
	Boolean confirmed = false
	Boolean active = false
	String defaultLanguage	
	String firstname, lastname, organisation, phoneNumber
	CalculationLocation location
	UserType userType
	
	static hasMany = [roles: Role]
	
	User() {
		roles = []
	}
	
	def getPermissions() {
		return Utils.split(permissionString, User.PERMISSION_DELIMITER)
	}
	
	def setPermissions(def permissions) {
		this.permissionString = Utils.unsplit(permissions, User.PERMISSION_DELIMITER)
	}
	
	def addToPermissions(def permission) {
		def permissions = getPermissions()
		permissions << permission
		this.permissionString = Utils.unsplit(permissions, User.PERMISSION_DELIMITER)
	}
		
	def removeFromPermissions(def permission) {
		def permissions = getPermissions()
		permissions.remove(permission)
		this.permissionString = Utils.unsplit(permissions, User.PERMISSION_DELIMITER)
	}
	
	public boolean canAccessCalculationLocation(CalculationLocation calculationLocation){
		if(calculationLocation instanceof Location && location instanceof DataLocation) return false
		if(calculationLocation instanceof DataLocation && location instanceof DataLocation) return calculationLocation == location
		if(calculationLocation instanceof Location && location instanceof Location && calculationLocation.level == location.level) return calculationLocation == location
		if(calculationLocation instanceof DataLocation && location instanceof Location && calculationLocation.location.level == location.level) return calculationLocation.location == location
		return locationService.getParentOfLevel(calculationLocation instanceof Location ? calculationLocation : calculationLocation.location, location.level) == location
	}
	
	def canActivate() {
		return confirmed == true && active == false
	}
	
    static constraints = {
		permissionString nullable: false
		email(email:true, unique: true, nullable: true)
        username(nullable: false, blank: false, unique: true)
		uuid(nullable: false, blank: false, unique: true)
		firstname(nullable: false, blank: false)
		lastname(nullable: false, blank: false)
		phoneNumber(phoneNumber: true, nullable: false, blank: false)
		organisation(nullable: false, blank: false)
		defaultLanguage(nullable: true)
		userType(nullable: false, blank: false)
		location(nullable: true)
		registrationToken(nullable: true)
		passwordToken(nullable: true)
		active(validator: {val, obj ->
			//return val ? obj.location != null && (obj.permissionString || obj.roles.size() > 0) : true
		})
    }
	
	static mapping = {
		permissionString type: 'text'
		table "memms_user"
		cache true
		version false
	}
	
}
