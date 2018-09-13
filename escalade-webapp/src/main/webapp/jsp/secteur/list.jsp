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
<h2>Liste des Secteur</h2>

<ul>
    <s:iterator value="listSecteur">
        <li>
            <div>ID : <s:property value="id"/></div>
            <div>Nom : <s:property value="nom"/></div>
            <div>Description : <s:property value="description"/></div>
                <%-- todo coordonnees GPS --%>
            <div>Site associé : <s:property value="siteId"/></div>
            <div><s:a action="detail_secteur">
                <s:param name="id" value="id"/>
                détail du site
            </s:a></div>
            <div><s:a action="update_secteur">
                <s:param name="id" value="id"/>
                modifier
            </s:a></div>
            <div><s:a action="delete_secteur">
                <s:param name="id" value="id"/>
                supprimer
            </s:a></div>
        </li>
    </s:iterator>
</ul>
</body>
</html>