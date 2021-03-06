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

import org.chai.memms.Initializer;
import org.chai.memms.IntegrationTests
import org.chai.memms.inventory.EquipmentType.Observation;
import org.chai.memms.inventory.EquipmentType;

class EquipmentTypeSpec extends IntegrationTests{

    def "can create and save an equipment type"() {
		setup:
		def observation = Observation.USEDINMEMMS;
		when:
		Initializer.newEquipmentType(CODE(123),["en":"testName"],["en":"testObservations"],observation,Initializer.now())
		def equipmentType = new EquipmentType(code:CODE(122),names:["en":"Accelerometers"],descriptions:["en":"used in memms"],observation:observation,lastModifiedOn:Initializer.now())
		equipmentType.save(failOnError: true)
		then:
		EquipmentType.count() == 2
	}
	
	def "can't create and save an equipment type without a code"() {
		setup:
		def observation = Observation.USEDINMEMMS;
		Initializer.newEquipmentType(CODE(123), ["en":"Accelerometers"],["en":"used in memms"],observation,Initializer.now())
		when:
		def equipmentType = new EquipmentType(names:["en":"Accelerometers"],descriptions:["en":"used in memms"],observation:observation,lastModifiedOn:Initializer.now())
		equipmentType.save()
		then:
		EquipmentType.count() == 1
		equipmentType.errors.hasFieldErrors('code') == true
	}
	
	def "can't create and save an equipment type with a duplicate code"() {
		setup:
		def observation = Observation.USEDINMEMMS;
		Initializer.newEquipmentType(CODE(123), ["en":"Accelerometers"],["en":"used in memms"],observation,Initializer.now())
		when:
		def equipmentType = new EquipmentType(code:CODE(123),names:["en":"Accelerometers"],descriptions:["en":"used in memms"],observation:observation,lastModifiedOn:Initializer.now())
		equipmentType.save()
		then:
		EquipmentType.count() == 1
		equipmentType.errors.hasFieldErrors('code') == true
	}
}
