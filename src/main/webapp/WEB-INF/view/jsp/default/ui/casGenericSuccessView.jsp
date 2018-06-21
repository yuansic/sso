<jsp:directive.include file="includes/top.jsp" />
<div id="msg" class="success">
	<h2>
		<spring:message code="screen.success.header" />
	</h2>
	<p>
		<spring:message code="screen.success.success"
			arguments="${principal.id}" />
	</p>
	<c:if test="${fn:length(principal.attributes) > 0}">
		<cas:principal.attributes>
			<c:forEach var="attr" items="${principal.attributes}" varStatus="loopStatus"
				begin="0" end="${fn:length(principal.attributes)}" step="1">
				<c:out value="${attr }"></c:out>
			</c:forEach>
		</cas:principal.attributes>
	</c:if>
	<c:if test="${fn:length(principal.attributes) == 0}">
          principal.attributes is null
    </c:if>

	<p>
		<spring:message code="screen.success.security" />
	</p>
</div>
<jsp:directive.include file="includes/bottom.jsp" />

