<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<meta charset="UTF-8">

<html>

<head>
    <%@include file="../_include/head.jsp" %>

</head>

<%@ include file="../_include/header.jsp"%>

<h5>Historique de réservation</h5>
<%-- todo lister les date de réservation sur ce topo --%>

<h5>Reserver ce topo</h5>
<s:a action="borrow">
    <s:param name="post" value="true"/>
    Emprunt 3 semaines
</s:a>

</html>
