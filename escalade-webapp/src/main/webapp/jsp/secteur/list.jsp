<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<meta charset="UTF-8">

<html>

<head>
    <%@include file="../_include/head.jsp" %>
</head>

<%@ include file="../_include/header.jsp" %>

<body>
<div class="container-fluid">
    <h2>Liste des Secteurs</h2>

    <ul class="list-group">
        <s:iterator value="listSecteur">
            <li class="list-group-item">
                <div><s:property value="nom"/></div>
                <div>Description : <s:property value="description"/></div>
                <div><s:a action="detail_secteur">
                    <s:param name="id" value="id"/>
                    Accéder au secteur
                </s:a></div>
            </li>
        </s:iterator>
    </ul>
</div>
</body>
</html>
