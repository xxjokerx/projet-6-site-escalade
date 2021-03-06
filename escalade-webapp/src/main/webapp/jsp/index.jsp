<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<meta charset="UTF-8">

<html>

<head>
    <%@include file="./_include/head.jsp" %>
</head>

<%@ include file="./_include/header.jsp" %>

<body>
<div class="container-fluid">

    <div class="jumbotron">
        <h1 class="display-4">Escalade.com</h1>
        <p class="lead">Bienvenu sur escalade.com, le site communautaire dédié à la pratique de la grimpe !</p>
        <hr class="my-4">
        <s:if test="#session.utilisateur">
            <p>Pour commencer, effectuez une recherche, ou affichez la liste des topos publiés. Vous pouvez accéder à
                votre espace de gestion d'emprunts.</p>
            <p class="lead">
                <a class="btn btn-primary btn-lg" href="check_borrow_and_loan.action" role="button">Gestionnaire
                    d'emprunt</a>
            </p>
        </s:if>
        <s:else>
            <p>Pour commencer, effectuez une recherche, ou affichez la liste des topos publiés. Enfin vous pouvez vous
                connecter pour un accès étendu au site.</p>
            <p class="lead">
                <a class="btn btn-primary btn-lg" href="login.action" role="button">Connectez-vous</a>
            </p>
        </s:else>
    </div>
</div>
</body>
</html>
