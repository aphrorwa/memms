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
 * 
 */
package org.chai.memms.maintenance

import groovy.transform.EqualsAndHashCode;

import java.util.Date;

import org.chai.memms.corrective.maintenance.CorrectiveProcess;
import org.chai.memms.inventory.Equipment;
import org.chai.memms.security.User;

/**
 * @author Jean Kahigiso M.
 * 
 */
@EqualsAndHashCode(includes='id')
public abstract class MaintenanceOrder {
	
	Date openOn
	Date closedOn
	Date addedOn
	Date lastModifiedOn
	
	User addedBy
	User lastModifiedBy
	
		
	static constraints = {
		
		openOn nullable: false, validator:{it <= new Date()}
		addedOn nullable: false, validator:{ it <= new Date()}
		closedOn nullable: true
		addedBy nullable: false
		
		lastModifiedOn nullable: true, validator:{ val, obj ->
			if(val!=null) return ((val <= new Date()) && (val.after(obj.openOn) || (val.compareTo(obj.openOn)==0)))
		}
		lastModifiedBy nullable: true, validator:{ val, obj ->
			if(val!=null) return (obj.lastModifiedOn!=null)
		}
		
	}
	
	static mapping = {
		table "memms_maintenance_order_abstract"
		tablePerHierarchy false
		version false
		cache true
	}
}
