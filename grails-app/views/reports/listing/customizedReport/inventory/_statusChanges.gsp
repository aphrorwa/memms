<%@ page import="org.chai.memms.inventory.EquipmentStatus.EquipmentStatusChange" %>
<ul>
  <li>
    %{-- TODO fix checkbox list styles !!! --}%
    <label for="statusChanges"><g:message code="reports.statusChanges"/>:</label>
    <ul class="checkbox-list">
	    <g:each in="${EquipmentStatusChange.values()}" var="statusEnum">
		    <li>
		        <input name="statusChanges" type="checkbox" value="${statusEnum}"/>
		        <label for="${statusEnum}">${message(code: statusEnum?.messageCode+'.'+statusEnum?.name)}</label>
		    </li>
	    </g:each>
	</ul>
  </li>
  <g:render template="/reports/listing/customizedReport/statusChangesPeriod"/>
</ul>