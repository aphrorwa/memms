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
package org.chai.memms.inventory

import javax.persistence.Transient;

import org.chai.memms.Contact;
import org.chai.memms.Period;
import org.chai.memms.Warranty;
import org.chai.memms.inventory.EquipmentStatus.Status;
import org.chai.memms.corrective.maintenance.WorkOrder;
import org.chai.location.DataLocation;
import org.apache.commons.lang.math.RandomUtils;

import groovy.transform.EqualsAndHashCode;
import i18nfields.I18nFields

/**
 * @author Jean Kahigiso M.
 *
 */
@i18nfields.I18nFields
@EqualsAndHashCode(includes="code")
public class Equipment {
	
	enum PurchasedBy{
		
		NONE('none'),
		BYMOH("by.moh"),
		BYFACILITY("by.facility"),
		BYDONOR("by.donor")
		
		String messageCode = "equipment.purchased"
		
		final String name
		PurchasedBy(String name){ this.name=name }
		String getKey() { return name() }
		
	}
	
	enum Donor{
		
		NONE('none'),
		MOHPARTNER("moh.partner"),
		OTHERNGO("other.ngo"),
		INDIVIDUAL("individual"),
		OTHERS("others")
		
		String messageCode = "equipment.donor"
		
		final String name
		Donor(String name){ this.name=name }
		String getKey() { return name() }
		
	}
	
	String serialNumber
	String currency
	String descriptions
	String model
	String room
	String code
	String donorName
	Double purchaseCost
	
	Period expectedLifeTime
	Period serviceContractPeriod
	Period warrantyPeriod
	Boolean obsolete
	
	Provider manufacturer
	Provider supplier
	Provider serviceProvider
	Warranty warranty
	
	Status currentStatus
	PurchasedBy purchaser
	Donor donor
	
	Date manufactureDate
	Date purchaseDate
	Date serviceContractStartDate
	Date registeredOn
	
	
	
	static hasMany = [status: EquipmentStatus, workOrders: WorkOrder]
	static belongsTo = [dataLocation: DataLocation, department: Department, type: EquipmentType]
	static i18nFields = ["observations","descriptions"]
	static embedded = ["warranty","serviceContractPeriod","expectedLifeTime","warrantyPeriod"]
	
	static constraints = {
		importFrom Contact
		code nullable: false, blank:false, unique:true
		model nullable: true
		currentStatus nullable:true,validator:{
			if(it!=null) return it in [Status.OPERATIONAL,Status.PARTIALLYOPERATIONAL,Status.INSTOCK,Status.UNDERMAINTENANCE,Status.FORDISPOSAL,Status.DISPOSED]
		}
		//TODO nullable has to be false, but it is true for first iteration
		supplier nullable: true
		//TODO nullable has to be false, but it is true for first iteration
		manufacturer nullable: true
		serviceProvider nullable: true, validator:{val, obj ->
			if(val == null) return (obj.serviceContractStartDate==null && obj.serviceContractPeriod==null)
		}
		warranty nullable: true
		warrantyPeriod nullable: true, validator:{val, obj ->
			if (obj.warranty!=null) return (val!=null)
		}
		
		serialNumber nullable: false, blank: false,  unique: true
		purchaseCost nullable: true, blank: true, validator:{ if(it!=null) return (it>0) }
		//TODO nullable has to be false, but it is true for first iteration
		//The value none have to be removed from valid answer
		purchaser nullable: false, inList:[PurchasedBy.NONE,PurchasedBy.BYFACILITY,PurchasedBy.BYMOH,PurchasedBy.BYDONOR]
		donor nullable:true,inList:[Donor.OTHERNGO,Donor.MOHPARTNER,Donor.OTHERS,Donor.INDIVIDUAL], validator:{ val, obj ->
			if(obj.purchaser == PurchasedBy.BYDONOR) return (val!=null)
		}
		donorName nullable:true,blank:true, validator:{val, obj ->
			if(obj.purchaser == PurchasedBy.BYDONOR || obj.donor !=null) return (val!=null && val!="")
		}
		currency  nullable: true, blank: true, inList: ["RWF","USD","EUR"], validator:{ val, obj ->
			if(obj.purchaseCost != null) return (val != null)
		}
		expectedLifeTime nullable: false
		serviceContractPeriod nullable: true, validator:{ val, obj ->
			if(val==null) return (obj.serviceContractStartDate==null && obj.serviceProvider==null)
		}
		serviceContractStartDate nullable: true, blank: true, validator:{ val, obj ->
			if(val!=null) return (val<=new Date() && (val.after(obj.purchaseDate) || (val.compareTo(obj.purchaseDate)==0)))
			if(val==null) return (obj.serviceContractPeriod==null && obj.serviceProvider==null)
		}
		room nullable: true, blank: true
		//TODO nullable has to be false, but it is true for first iteration
		manufactureDate nullable: true, blank: false, validator:{it <= new Date()}
		//TODO nullable has to be false, but it is true for first iteration
		purchaseDate nullable: true, blank: false, validator:{ val, obj ->
			//TODO uncomment when fix
			//return  ((val <= new Date()) && val.after(obj.manufactureDate) || (val.compareTo(obj.manufactureDate)==0))
		}
		registeredOn nullable: false, blank:false
		descriptions nullable: true, blank: true
		obsolete nullable: false
	}
	
	static mapping = {
		table "memms_equipment"
		version false
	}
	
	@Transient
	def genarateAndSetEquipmentCode() {
		if(!code){
			def randomInt = RandomUtils.nextInt(99999)
			def now = new Date()
			def equipmentCode = "${dataLocation.code}-${randomInt}-${now.month}-${now.year+1900}"
			if(log.isDebugEnabled()) log.debug("Generated code:" + equipmentCode)
			if(Equipment.findByCode(equipmentCode.toString()) == null) code = equipmentCode 
			else genarateEquipmentCode()
		}
	}
	
	@Transient
	def getTimeBasedStatus(){
		if(!status) return null
		EquipmentStatus currentState = status.asList()[0]
		for(EquipmentStatus state : status){
			//To make sure we only compare date not time
			currentState.dateOfEvent.clearTime()
			state.dateOfEvent.clearTime()
			if(state.dateOfEvent.after(currentState.dateOfEvent)){
				currentState= state;
			}
			if(state.dateOfEvent.compareTo(currentState.dateOfEvent)==0){
				if(state.statusChangeDate.after(currentState.statusChangeDate))
					currentState = state
				//This case happen in test data settings
				if(state.statusChangeDate.compareTo(currentState.statusChangeDate)==0)
					currentState = (currentState.id > state.id)?currentState:state
			}
			
		}
		return currentState
	}
	
	
	String toString() {
		return "Equipment[id=" + id + " currentState="+currentStatus+"]";
	}
}
